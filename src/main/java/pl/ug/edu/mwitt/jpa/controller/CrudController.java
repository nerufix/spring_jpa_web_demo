package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.PersonType;
import pl.ug.edu.mwitt.jpa.service.MatchService;
import pl.ug.edu.mwitt.jpa.service.PersonService;
import pl.ug.edu.mwitt.jpa.service.TeamService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
public class CrudController {

    @Autowired
    PersonService persons;

    @Autowired
    MatchService matches;

    @Autowired
    TeamService teams;

    @ModelAttribute("user")
    public Person user(HttpServletRequest request) {
        Object attr = request.getAttribute("user");
        if (attr instanceof Person) {
            return (Person) attr;
        }
        return null;
    }

    @GetMapping("/addPerson")
    public String addPerson(HttpServletRequest request,
                            @RequestParam(required=false) String id,
                            Model model){
        model.addAttribute("allTeams", teams.findAll());
        if (id!=null) {
            Optional<Person> person = persons.findByIdTransactional(id);
            person.ifPresent(value -> model.addAttribute("person", value));
            return "addPerson";
        }
        model.addAttribute("person", new Person());
        return "addPerson";
    }

    @PostMapping("/addPerson")
    public String sendAddPerson(@Valid Person person, Errors errors) {
        System.out.println(errors.getAllErrors());
        if(errors.hasErrors()){
            return "addPerson";
        }
        System.out.println(person.getPersonType());
        if (person.getId()!=null) {
            persons.getRepo().updateNameAndPasswordAndPersonTypeAndTeamById(
                    person.getName(),
                    Hashing.sha256()
                            .hashString(person.getPassword(), StandardCharsets.UTF_8)
                            .toString(),
                    person.getPersonType(),
                    person.getTeam(),
                    person.getId()
            );
            return "success";
        }
        persons.getRepo().save(person);
        return "success";
    }
}
