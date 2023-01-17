package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Bet {

    private Long id;

    @NotNull
    @Min(0)
    private Double amount;
    @NotNull
    private Person person;
    @NotNull
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "person")
    public Person getPerson() {
        return person;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
