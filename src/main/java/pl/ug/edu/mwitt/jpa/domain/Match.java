package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Match {

    private Long id;
    private Set<Person> persons;
    private MatchType matchType;
    private Person winner;
    private Timestamp beginTime;

    @ManyToMany(mappedBy="matches", fetch = FetchType.LAZY)
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
