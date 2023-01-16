package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.ug.edu.mwitt.jpa.domain.Match;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.PersonType;
import pl.ug.edu.mwitt.jpa.service.MatchService;
import pl.ug.edu.mwitt.jpa.service.PersonService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    PersonService persons;

    @Autowired
    MatchService matches;

    @ModelAttribute("user")
    public Person user(HttpServletRequest request) {
        Object attr = request.getAttribute("user");
        if (attr instanceof Person) {
            return (Person) attr;
        }
        return null;
    }

    @PostMapping("/logout")
    public String sendLogout(Person person,
                            HttpServletResponse response,
                            Model model) {
        response.addCookie(new Cookie("session", null));
        return "redirect:/";

    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request,
                            Model model){
//        Person user = (Person) request.getAttribute("user");
//        model.addAttribute("user", user);

        model.addAttribute("upcomingMatches", matches.findUpcomingMatches());
        model.addAttribute("mostBettedMatches", matches.findMostBettedMatches());
        return "dashboard";
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request,
                            Model model){
        return "search";
    }

    @PostMapping("/searchMatch")
    public String sendSearchMatch(HttpServletRequest request,
                             @RequestParam String contestant,
                             @RequestParam String beginDate,
                             @RequestParam String endDate,
                             @RequestParam(required=false) String matchType,
                             Model model){
        model.addAttribute("foundMatches", matches.findBySearch(contestant, beginDate, endDate, matchType));
        return "search";
    }

    @PostMapping("/searchPerson")
    public String sendSearchPerson(HttpServletRequest request,
                             @RequestParam String name,
                             @RequestParam String team,
                             Model model){
        model.addAttribute("foundPeople", persons.findBySearch(name, team));
        return "search";
    }


}
