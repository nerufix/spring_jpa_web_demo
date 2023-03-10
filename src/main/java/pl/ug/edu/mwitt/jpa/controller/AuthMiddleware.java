package pl.ug.edu.mwitt.jpa.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.PersonType;
import pl.ug.edu.mwitt.jpa.service.PersonService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AuthMiddleware extends OncePerRequestFilter {

    @Autowired
    PersonService persons;

    List<String> guestPassURIs = List.of("", "/", "/login", "/register", "/css/style.css");
    List<String> adminPassURIs = List.of("/addPerson", "/deletePerson",
                                         "/addMatch", "/deleteMatch",
                                         "/addTeam", "/deleteTeam");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (!guestPassURIs.contains(request.getRequestURI())) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (name.equals("session") && !value.equals("")) {
                    Optional<Person> user = persons.findByIdTransactional(value);
                    //user.ifPresent(person -> request.setAttribute("user", person));
                    if (user.isPresent()) {
                        request.setAttribute("user", user.get());
                        if (adminPassURIs.contains(request.getRequestURI())
                        && !user.get().getPersonType().equals(PersonType.Admin)) {
                            break;
                        }
                    } else {
                        break;
                    }
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            response.sendRedirect("/");
        }
        filterChain.doFilter(request, response);
    }

}