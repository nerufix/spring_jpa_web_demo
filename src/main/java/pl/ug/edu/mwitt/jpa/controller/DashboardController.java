package pl.ug.edu.mwitt.jpa.controller;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

//    @ModelAttribute("user")
//    public Person user(HttpServletRequest request) {
//        Object attr = request.getAttribute("user");
//        System.out.println("att");
//        System.out.println(attr);
//        if (attr instanceof Person) {
//            System.out.println("is person");
//            return (Person) attr;
//        }
//        return null;
//    }

    @PostMapping("/logout")
    public String sendLogout(Person person,
                            HttpServletResponse response,
                            Model model) {
        response.addCookie(new Cookie("session", null));
        return "redirect:/";

    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request,
                            @CookieValue(value = "session", defaultValue = "-1") Long session,
                            Model model){
        Person user = (Person) request.getAttribute("user");
        System.out.println(request.getAttribute("user"));
        model.addAttribute("user", user);
        return "dashboard";
    }

}
