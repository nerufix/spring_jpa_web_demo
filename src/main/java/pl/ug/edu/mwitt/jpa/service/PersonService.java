package pl.ug.edu.mwitt.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.repository.PersonRepository;

import java.sql.Date;
import java.util.List;

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

    public void importMany(List<Person> persons) {
        personRepo.saveAll(persons);
    }
}
