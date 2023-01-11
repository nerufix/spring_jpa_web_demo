package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.Match;

import java.util.Optional;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    Optional<Match> findById(Long id);

}
