package pl.ug.edu.mwitt.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.Match;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.repository.PersonRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
            Hibernate.initialize(person.get().getMatches());
            Hibernate.initialize(person.get().getBets());
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
}
