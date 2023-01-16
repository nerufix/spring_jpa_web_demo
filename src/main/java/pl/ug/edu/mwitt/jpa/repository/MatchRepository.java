package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.Match;
import pl.ug.edu.mwitt.jpa.domain.MatchType;
import pl.ug.edu.mwitt.jpa.domain.MatchValueDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    Optional<Match> findById(Long id);

    List<Match> findFirst3ByBeginTimeGreaterThanOrderByBeginTimeAsc(Timestamp timestamp);

//    @Query(value = """
//            select new pl.ug.edu.mwitt.jpa.domain.MatchValueDTO(
//            m,
//            (select sum(b.amount) from Bet b where b.match.id = m.id) as total
//            ) from Match m order by total desc
//            """)

    @Query(value = """
            select new pl.ug.edu.mwitt.jpa.domain.MatchValueDTO(
            m,
            sum(b.amount) as amount
            ) from Match m right join Bet b on b.match=m.id group by m order by amount desc
            """)
    List<MatchValueDTO> findMostBettedMatches();

    List<Match> findAll();

    List<Match> findByPersons_NameContaining(String name);

    List<Match> findByBeginTimeGreaterThanEqual(Timestamp toDate);

    List<Match> findByBeginTimeLessThanEqual(Timestamp toDate);

    List<Match> findByMatchType(MatchType matchType);

}
