package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.BetSumOnPersonDTO;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {

    Optional<Bet> findById(Long id);

    @Override
    void deleteById(Long id);

    List<Bet> findByMatch_Id(Long id);

    @Query(value = """
            select new pl.ug.edu.mwitt.jpa.domain.BetSumOnPersonDTO(
            p.id,
            sum(b.amount) as amount
            ) from Bet b left join b.match.persons p where b.match.id=?1 and p.id=b.person group by p.id
            """)
    List<BetSumOnPersonDTO> findSumOnPersonByMatchId(Long id);


}
