package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ug.edu.mwitt.jpa.domain.*;
import pl.ug.edu.mwitt.jpa.service.BetService;
import pl.ug.edu.mwitt.jpa.service.MatchService;
import pl.ug.edu.mwitt.jpa.service.PersonService;
import pl.ug.edu.mwitt.jpa.service.TeamService;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class CrudController {

    @Autowired
    PersonService persons;

    @Autowired
    MatchService matches;

    @Autowired
    TeamService teams;

    @Autowired
    BetService bets;

    @ModelAttribute("user")
    public Person user(HttpServletRequest request) {
        Object attr = request.getAttribute("user");
        if (attr instanceof Person) {
            return (Person) attr;
        }
        return null;
    }

    @PostMapping("/deletePerson")
    public String deletePerson(HttpServletRequest request,
                            @RequestParam String id,
                            Model model){
        List<Match> personMatches = matches.findByPersons_Id(id);
        if (personMatches.size()==0) {
            persons.getRepo().deleteById(Long.valueOf(id));
            return "success";
        }
        model.addAttribute("bet", new Bet());
        List<Long> foundMatchesUserBetsIds = personMatches
                .stream()
                .map(m -> persons.isMatchInPersonBets(
                        m, (Person) Objects.requireNonNull(model.getAttribute("user"))
                )).toList();
        List<Optional<Bet>> foundMatchesUserBets = foundMatchesUserBetsIds
                .stream()
                .map(b_id -> bets.findByIdTransactional(b_id))
                .toList();
        model.addAttribute("foundMatchesUserBets", foundMatchesUserBets);
        model.addAttribute("foundMatches", personMatches);
        model.addAttribute("cannotDeletePerson", true);
        return "search";
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
            String password = person.getPassword();
            if (person.getPassword().length()!=64) {
                password = Hashing.sha256()
                        .hashString(person.getPassword(), StandardCharsets.UTF_8)
                        .toString();
            }
            persons.getRepo().updateNameAndPasswordAndPersonTypeAndTeamById(
                    person.getName(),
                    password,
                    person.getPersonType(),
                    person.getTeam(),
                    person.getId()
            );
            return "success";
        }
        persons.getRepo().save(person);
        return "success";
    }

    @GetMapping("/addMatch")
    public String addMatch(HttpServletRequest request,
                            @RequestParam(required=false) String id,
                            Model model){
        model.addAttribute("allPlayers", persons.getRepo().findByPersonType(PersonType.Player));

        if (id!=null) {
            MatchFormDTO match = matches.convertMatchToFormDTO(id);
            Set<Person> matchPersons = matches.findByIdTransactional(id).get().getPersons();
            System.out.println(match);
            model.addAttribute("match", match);
            model.addAttribute("matchPersons", matchPersons);
            return "addMatch";
        }
        model.addAttribute("match", new MatchFormDTO());
        return "addMatch";
    }

    @PostMapping("/addMatch")
    public String sendAddMatch(@Valid MatchFormDTO match,
                               Errors errors,
                               Model model) {
        model.addAttribute("match", match);
        model.addAttribute("fields", errors);
        model.addAttribute("allPlayers", persons.getRepo().findByPersonType(PersonType.Player));
        if(errors.hasErrors()){
            return "addMatch";
        }

//        if (match.getId()!=null) {
//            return "success";
//        }
        matches.getRepo().save(persons.convertFormDTO(match));
        return "success";
    }

    @PostMapping("/deleteMatch")
    public String deleteMatch(HttpServletRequest request,
                               @RequestParam String id,
                               Model model){
//        Match match = matches.findByIdTransactional(id).get();
//        match.setWinner(null);
//        matches.getRepo().save(match);
//        persons.deleteMatchAssociations(Long.valueOf(id));
        bets.getRepo().findByMatch_Id(Long.valueOf(id)).forEach(b -> {
            persons.deleteBetAssociation(b.getId());
            bets.getRepo().deleteById(b.getId());
        });

        matches.getRepo().deleteById(Long.valueOf(id));
        return "success";
    }

    @GetMapping("/addTeam")
    public String addTeam(Model model, @RequestParam(required=false) String id) {
        if (id!=null) {
            model.addAttribute("team", teams.getRepo().findById(Long.valueOf(id)).get());
        } else {
            model.addAttribute("team", new Team());
        }
        return "addTeam";
}

    @PostMapping("/addTeam")
    public String sendAddTeam(@Valid Team team,
                               Errors errors,
                               Model model) {
        if(errors.hasErrors()){
            return "addTeam";
        }
        teams.getRepo().save(team);
        return "success";
    }

    @PostMapping("/deleteTeam")
    public String deleteTeam(HttpServletRequest request,
                               @RequestParam String id,
                               Model model){
        Set<Person> teamPeople = teams.findByIdTransactional(id).getPersons();
        if (teamPeople.size()==0) {
            teams.getRepo().deleteById(Long.valueOf(id));
            return "success";
        }
        model.addAttribute("foundPeople", teamPeople);
        model.addAttribute("cannotDeleteTeam", true);
        return "search";
    }
}
