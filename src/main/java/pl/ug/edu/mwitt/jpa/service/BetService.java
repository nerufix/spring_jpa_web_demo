package pl.ug.edu.mwitt.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.Team;
import pl.ug.edu.mwitt.jpa.repository.BetRepository;
import pl.ug.edu.mwitt.jpa.repository.TeamRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BetService {

    @Autowired
    final BetRepository betRepo;


    public BetService(BetRepository betRepo) {
        this.betRepo = betRepo;
    }

    public BetRepository getRepo() {
        return betRepo;
    }

    @Transactional
    public Optional<Bet> findByIdTransactional(Long id) {
        Optional<Bet> query = betRepo.findById(id);
        query.ifPresent(bet -> Hibernate.initialize(bet.getPerson()));
        return query;
    }

    public void importMany(List<Bet> bets) {
        System.out.println(bets);
        betRepo.saveAll(bets);
    }
}
