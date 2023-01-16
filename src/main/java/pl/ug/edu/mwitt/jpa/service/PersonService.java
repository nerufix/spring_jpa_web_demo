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
    public Person findByIdTransactional(String id) {
        Person person = personRepo.findById(Long.parseLong(id)).get();
        Hibernate.initialize(person.getMatches());
        return person;
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
