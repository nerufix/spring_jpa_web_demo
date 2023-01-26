package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Match {

    private Long id;
    private Set<Person> persons;
    @NotNull
    private MatchType matchType;
    private Person winner;
    @NotNull
    private Timestamp beginTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="person_match")
    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }


    public Match() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner")
    public Person getWinner() { return winner; }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setWinner(Person winner) {
        this.winner = winner;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }
}
