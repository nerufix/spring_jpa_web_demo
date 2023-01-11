package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.PersonType;
import pl.ug.edu.mwitt.jpa.service.PersonService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    PersonService persons;


    @PostMapping("/register")
    public String sendRegister(@Valid Person person, Errors errors) {
        System.out.println(errors.getAllErrors());
        if(errors.hasErrors()){
            return "register";
        }
        person.setPersonType(PersonType.Guest);
        persons.getRepo().save(person);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String sendLogin(Person person,
                            HttpServletResponse response,
                            Model model) {
        List<Person> query = persons.getRepo().findByName(person.getName());
        String hashedPassword = Hashing.sha256()
                .hashString(person.getPassword(), StandardCharsets.UTF_8)
                .toString();
        if (!query.isEmpty() && Objects.equals(query.get(0).getPassword(), hashedPassword)) {
            response.addCookie(new Cookie("session", query.get(0).getId().toString()));
            return "redirect:/";
        }
        model.addAttribute("invalidLogin", "1");
        return "login";

    }

    @GetMapping("/")
    public String init(@CookieValue(value = "session", defaultValue = "-1") Long session,
                       Model model){
        Optional<Person> query = persons.getRepo().findById(session);
        if (query.isPresent()) {
            model.addAttribute("user", query.get());
            return "dashboard";
        }
        return "welcome";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("person", new Person());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("person", new Person());
        return "register";
    }

}
