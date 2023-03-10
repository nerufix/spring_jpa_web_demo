package pl.ug.edu.mwitt.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ug.edu.mwitt.jpa.domain.*;
import pl.ug.edu.mwitt.jpa.repository.MatchRepository;
import pl.ug.edu.mwitt.jpa.repository.TeamRepository;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchService {

    @Autowired
    final MatchRepository matchRepo;

    public MatchService(MatchRepository matchRepo) {
        this.matchRepo = matchRepo;
    }

    public MatchRepository getRepo() {
        return matchRepo;
    }

    public void importMany(List<Match> matches) {
        matchRepo.saveAll(matches);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<Match> findUpcomingMatches() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        List<Match> upcomingMatches = matchRepo.findFirst3ByBeginTimeGreaterThanOrderByBeginTimeAsc(now);
        for (Match match: upcomingMatches) {
            Hibernate.initialize(match.getPersons());
        }
        return upcomingMatches;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<MatchValueDTO> findMostBettedMatches() {
        List<MatchValueDTO> mostBettedMatches = matchRepo.findMostBettedMatches();
        for (MatchValueDTO dto: mostBettedMatches) {
            Hibernate.initialize(dto.getMatch().getPersons());
        }

        return mostBettedMatches.subList(0, Integer.min(mostBettedMatches.size(), 3));
    }

    @Transactional
    public List<Match> findByPersons_Id(String id) {
        List<Match> matches = matchRepo.findByPersons_Id(Long.valueOf(id));
        for (Match m : matches) {
            Hibernate.initialize(m.getPersons());
            Hibernate.initialize(m.getWinner());
        }
        return matches;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<Match> findBySearch(String contestant,
                                    String beginDate,
                                    String endDate,
                                    String matchType) {
        List<Match> query = matchRepo.findAll();

        if (!beginDate.isEmpty()) {
            System.out.println(beginDate);
            query.retainAll(matchRepo.findByBeginTimeGreaterThanEqual(Timestamp.valueOf(beginDate+" 00:00:00")));
        }
        if (!contestant.isEmpty()) {
            System.out.println(contestant);
            query.retainAll(matchRepo.findByPersons_NameContaining(contestant));
        }
        if (!endDate.isEmpty()) {
            System.out.println(endDate);
            query.retainAll(matchRepo.findByBeginTimeLessThanEqual(Timestamp.valueOf(endDate+" 23:59:59")));
        }
        if (matchType!=null) {
            System.out.println(matchType);
            query.retainAll(matchRepo.findByMatchType(MatchType.fromString(matchType)));
        }

        for (Match match : query) {
            Hibernate.initialize(match.getPersons());
            Hibernate.initialize(match.getWinner());
        }

        return query;
    }

    @Transactional
    public Optional<Match> findByIdTransactional(String id) {
        Optional<Match> match = matchRepo.findById(Long.parseLong(id));
        if (match.isPresent()) {
            Hibernate.initialize(match.get().getWinner());
            Hibernate.initialize(match.get().getPersons());
        }
        return match;
    }

    @Transactional
    public MatchFormDTO convertMatchToFormDTO(String id) {
        MatchFormDTO dto = new MatchFormDTO();
        Match match = findByIdTransactional(id).get();
        dto.setId(match.getId().toString());
        dto.setMatchType(match.getMatchType());
        Iterator<Person> persons = match.getPersons().stream().iterator();
        dto.setPerson1(persons.next().getId().toString());
        dto.setPerson2(persons.next().getId().toString());
        dto.setBeginTime(match.getBeginTime().toString()
                .replace(" ", "T")
                .substring(0, match.getBeginTime().toString().length()-5));
        if (match.getWinner()!=null) {
            dto.setWinner(match.getWinner().getId().toString());
        }
        return dto;
    }
}
