package pl.ug.edu.mwitt.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.*;
import pl.ug.edu.mwitt.jpa.repository.PersonRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PersonService {

    @Autowired
    final PersonRepository personRepo;

    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    public PersonRepository getRepo() {
        return personRepo;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public Optional<Person> findByIdTransactional(String id) {
        Optional<Person> person = personRepo.findById(Long.parseLong(id));
        if (person.isPresent()) {
//            Hibernate.initialize(person.get().getMatches());
            Hibernate.initialize(person.get().getBets());
            Hibernate.initialize(person.get().getTeam());
        }
        return person;
    }

    public Long isMatchInPersonBets(Match match, Person person) {
        List<Long> query =  personRepo.findBets_IdByIdAndBets_Match_Id(person.getId(), match.getId());
        if (query.size()>0) {
            return query.get(0);
        }
        return 0L;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<Person> findBySearch(String name, String team) {
        List<Person> query = personRepo.findAll();

        if (!name.isEmpty()) {
            query.retainAll(personRepo.findByNameContaining(name));
        }
        if (!team.isEmpty()) {
            query.retainAll(personRepo.findByTeam_NameContaining(team));
        }

        for (Person person : query) {
            Hibernate.initialize(person.getTeam());
        }
        return query;
    }

    public void importMany(List<Person> persons) {
        personRepo.saveAll(persons);
    }

    public void importBets(Person person, List<Bet> bets) {
        //
        for (Bet b : bets) {
            person.getBets().add(b);
            System.out.println(person.getBets());
        }
        personRepo.save(person);
    }

    public Match convertFormDTO(MatchFormDTO dto) {
        Match match = new Match();
        match.setPersons(new HashSet<>());
        match.getPersons().add(personRepo.findById(Long.valueOf(dto.getPerson1())).get());
        match.getPersons().add(personRepo.findById(Long.valueOf(dto.getPerson2())).get());
        if (dto.getWinner() != null) {
            match.setWinner(personRepo.findById(Long.valueOf(dto.getWinner())).get());
        }
        if (dto.getId() != null) {
            match.setId(Long.valueOf(dto.getId()));
        }
        match.setMatchType(dto.getMatchType());
        match.setBeginTime(Timestamp.valueOf(dto.getBeginTime().replace("T", " ") + ":00"));
        System.out.println(match.getBeginTime());

        return match;
    }

    @Transactional
    public void deleteBetAssociation(Long id) {
        personRepo.deleteBetAssociation(id);
    }

    @Transactional
    public Double getBalance(Long id) {
        Person person = personRepo.findById(id).get();
        Hibernate.initialize(person.getBets());
        Set<Bet> bets = person.getBets();
        Double balance = 0d;
        for (Bet bet : bets) {
            Hibernate.initialize(bet.getMatch());
            Match match = bet.getMatch();
            Hibernate.initialize(bet.getPerson());
            Hibernate.initialize(match.getWinner());
            if (match.getWinner()!=null) {
                List<BetSumOnPersonDTO> betSums = personRepo.findSumOnPersonByMatchId(match.getId());
                if (match.getWinner()==bet.getPerson()) {
                    Double loserAmount = betSums
                            .stream()
                            .filter(b -> !Objects.equals(b.getPersonId(), match.getWinner().getId())).toList()
                            .get(0).getSum();
                    Double winnerAmount = betSums
                            .stream()
                            .filter(b -> Objects.equals(b.getPersonId(), match.getWinner().getId())).toList()
                            .get(0).getSum();
                    Double loserToWinnerRatio = loserAmount/winnerAmount;
                    balance += bet.getAmount()+bet.getAmount()*loserToWinnerRatio;
                } else {
                    balance -= bet.getAmount();
                }
            }
        }
        return balance;
    }

}
