package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ug.edu.mwitt.jpa.service.PersonService;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MatchFormDTO {

    String id;
    @NotNull
    String person1;
    @NotNull
    String person2;
    @NotNull
    MatchType matchType;
    String winner;
    @NotNull(message = "Begin time cannot be null")
    @Size(min = 16, max=16, message = "Begin time must contain a complete timestamp")
    String beginTime;


    @AssertTrue(message = "Participants cannot be the same person")
    private boolean isNotSamePerson() {
        return person1!=null && !(person1.equals(person2));
    }

}
