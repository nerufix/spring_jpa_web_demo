package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findById(Long id);

}
