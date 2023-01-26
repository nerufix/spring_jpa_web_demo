package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;

@Entity
public class Team {

    private Long id;
    @NotNull(message = "Name cannot be empty")
    private String name;
    private Set<Person> people;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    public Set<Person> getPersons() {
        return people;
    }

    public Team() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
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

    public void setPersons(Set<Person> people) {
        this.people = people;
    }
}
