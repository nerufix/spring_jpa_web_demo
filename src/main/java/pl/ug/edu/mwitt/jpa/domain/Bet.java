package pl.ug.edu.mwitt.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.CascadeType.PERSIST;

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
    //@NotNull
//    private Person better;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name="person")
    public Person getPerson() {
        return person;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "match")
    public Match getMatch() {
        return match;
    }

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @JoinColumn(name="better")
//    public Person getBetter() { return better; }
//
//    public void setBetter(Person better) { this.better = better; }

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
