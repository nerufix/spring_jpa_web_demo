package pl.ug.edu.mwitt.jpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    @Transactional
    @Modifying
    @Query("update Person p set p.name = ?1, p.password = ?2, p.personType = ?3, p.team = ?4 where p.id = ?5")
    int updateNameAndPasswordAndPersonTypeAndTeamById(String name, String password, PersonType personType, Team team, Long id);
    @Transactional
    @Modifying
    @Query("update Person p set p.name = ?1, p.password = ?2, p.personType = ?3")
    int updateNameAndPasswordAndPersonTypeBy(String name, String password, PersonType personType);

    List<Person> findByName(String name);
    List<Person> findAll();
    List<Person> findByPassword(String password);
    Optional<Person> findById(Long id);

    List<Person> findByNameContaining(String name);
    List<Person> findByTeam_NameContaining(String team);

    //List<Bet> findBetsById(Long id);

    //@Query("select b.id from Bet b where b.better=?1 and b.match=?2")
    //List<Long> findBets_IdByIdAndBets_Match_Id(Long personId, Long matchId);

    @Query(value = "select b.id from Person p join p.bets b where p.id= ?1 and b.match.id= ?2")
    List<Long> findBets_IdByIdAndBets_Match_Id(Long personId, Long matchId);


}
