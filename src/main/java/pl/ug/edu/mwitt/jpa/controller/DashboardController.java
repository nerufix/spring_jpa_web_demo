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
import pl.ug.edu.mwitt.jpa.domain.*;
import pl.ug.edu.mwitt.jpa.service.BetService;
import pl.ug.edu.mwitt.jpa.service.MatchService;
import pl.ug.edu.mwitt.jpa.service.PersonService;
import pl.ug.edu.mwitt.jpa.service.TeamService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    PersonService persons;

    @Autowired
    MatchService matches;

    @Autowired
    BetService bets;

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
        model.addAttribute("bet", new Bet());
        List<Match> upcomingMatches = matches.findUpcomingMatches();
        List<MatchValueDTO> mostBettedMatches = matches.findMostBettedMatches();
        List<Match> mostBettedMatchesInclusive = mostBettedMatches.stream().map(MatchValueDTO::getMatch).toList();
        List<Long> userBetInUpcomingMatches = upcomingMatches
                .stream()
                .map(m -> persons.isMatchInPersonBets(
                        m, (Person) Objects.requireNonNull(model.getAttribute("user"))
                )).toList();
        List<Optional<Bet>> upcomingMatchesUserBets = userBetInUpcomingMatches
                .stream()
                .map(id -> bets.findByIdTransactional(id))
                .toList();
        List<Long> userBetInMostBettedMatches = mostBettedMatchesInclusive
                .stream()
                .map(m -> persons.isMatchInPersonBets(
                        m, (Person) Objects.requireNonNull(model.getAttribute("user"))
                )).toList();
        List<Optional<Bet>> mostBettedMatchesUserBets = userBetInMostBettedMatches
                .stream()
                .map(id -> bets.findByIdTransactional(id))
                .toList();
        Person user = (Person) model.getAttribute("user");

        model.addAttribute("upcomingMatchesUserBets", upcomingMatchesUserBets);
        model.addAttribute("mostBettedMatchesUserBets", mostBettedMatchesUserBets);
        model.addAttribute("upcomingMatches", upcomingMatches);
        model.addAttribute("mostBettedMatches", mostBettedMatches);
        model.addAttribute("balance", persons.getBalance(user.getId()));
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
        model.addAttribute("bet", new Bet());
        List<Match> foundMatches = matches.findBySearch(contestant, beginDate, endDate, matchType);
        List<Long> foundMatchesUserBetsIds = foundMatches
                .stream()
                .map(m -> persons.isMatchInPersonBets(
                        m, (Person) Objects.requireNonNull(model.getAttribute("user"))
                )).toList();
        List<Optional<Bet>> foundMatchesUserBets = foundMatchesUserBetsIds
                .stream()
                .map(id -> bets.findByIdTransactional(id))
                .toList();
        model.addAttribute("foundMatchesUserBets", foundMatchesUserBets);
        model.addAttribute("foundMatches", foundMatches);
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

    @PostMapping("/searchTeam")
    public String sendSearchTeam(HttpServletRequest request,
                                   @RequestParam String name,
                                   Model model){
        model.addAttribute("foundTeams", teams.findBySearch(name));
        return "search";
    }

    @PostMapping("/dashboard/bet")
    public String sendBetPost(@Valid Bet bet, Errors errors, Model model){
        System.out.println(errors.getAllErrors());
        if(!errors.hasErrors()){
            Person user = (Person) model.getAttribute("user");
            assert user != null;
            user.getBets().add(bet);
            persons.getRepo().save(user);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/bet/delete")
    public String sendBetDelete(@RequestParam Long id, Model model){
        Person user = (Person) model.getAttribute("user");
        assert user != null;
        user.getBets().remove(user.getBets().stream().filter(b -> Objects.equals(b.getId(), id)).toList().get(0));
        persons.getRepo().save(user);
        return "redirect:/dashboard";
    }

    @PostMapping("/search/bet")
    public String sendBetPostSearch(@Valid Bet bet, Errors errors, Model model){
        System.out.println(errors.getAllErrors());
        if(!errors.hasErrors()){
            Person user = (Person) model.getAttribute("user");
            assert user != null;
            user.getBets().add(bet);
            persons.getRepo().save(user);
        }
        return "redirect:/search";
    }

    @PostMapping("/search/bet/delete")
    public String sendBetDeleteSearch(@RequestParam Long id, Model model){
        Person user = (Person) model.getAttribute("user");
        assert user != null;
        user.getBets().remove(user.getBets().stream().filter(b -> Objects.equals(b.getId(), id)).toList().get(0));
        persons.getRepo().save(user);
        return "redirect:/search";
    }

}
