package pl.ug.edu.mwitt.jpa.domain;

import com.google.common.hash.Hashing;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;

@Entity
public class Person {
    private Long id;
    @Column(length = 40, unique = true)
    @NotNull(message = "Field cannot be empty.")
    @Size(min = 3, max = 40, message = "Name must contain between 3 and 15 characters.")
    private String name;
    @Column(length = 64)
    @NotNull(message = "Field cannot be empty.")
    @Size(min = 5, message = "Password must contain at least 5 characters.")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit.")
    private String password;
    private Set<Match> matches;
    private Set<Bet> bets;
    private PersonType personType;
    private Team team;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.password = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    @ManyToMany(mappedBy="persons", fetch = FetchType.LAZY)
    public Set<Match> getMatches() {
        return matches;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team")
    public Team getTeam() {
        return team;
    }

    public Person() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @OneToMany(cascade=ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    //@JoinColumn(name="better")
    public Set<Bet> getBets() {
        return bets;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public void setBets(Set<Bet> bets) {
        this.bets = bets;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String toString() {
        return name+" "+personType;
    }
}
