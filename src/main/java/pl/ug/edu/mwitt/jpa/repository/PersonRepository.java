package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ug.edu.mwitt.jpa.domain.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByName(String name);
    List<Person> findByPassword(String password);
    Optional<Person> findById(Long id);

}
