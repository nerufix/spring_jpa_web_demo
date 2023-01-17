package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.Team;

import java.util.Optional;

@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {

    Optional<Bet> findById(Long id);

    @Override
    void deleteById(Long id);


}
