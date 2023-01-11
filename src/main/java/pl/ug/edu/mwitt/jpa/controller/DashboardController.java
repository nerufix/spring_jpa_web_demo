package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.PersonType;
import pl.ug.edu.mwitt.jpa.service.PersonService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    PersonService persons;

    @PostMapping("/logout")
    public String sendLogout(Person person,
                            HttpServletResponse response,
                            Model model) {
        response.addCookie(new Cookie("session", null));
        return "redirect:/";

    }

}
