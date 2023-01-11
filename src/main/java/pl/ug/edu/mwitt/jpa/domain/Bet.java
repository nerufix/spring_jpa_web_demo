package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class Bet {

    private Long id;
    @Min(0)
    private float amount;
    private Person person;
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person")
    public Person getPerson() {
        return person;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match")
    public Match getMatch() {
        return match;
    }

    public Bet() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
