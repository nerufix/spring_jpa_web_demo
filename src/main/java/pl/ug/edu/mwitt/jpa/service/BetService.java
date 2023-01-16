package pl.ug.edu.mwitt.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ug.edu.mwitt.jpa.domain.Bet;
import pl.ug.edu.mwitt.jpa.domain.Team;
import pl.ug.edu.mwitt.jpa.repository.BetRepository;
import pl.ug.edu.mwitt.jpa.repository.TeamRepository;

import java.util.List;

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

    public void importMany(List<Bet> bets) {
        System.out.println(bets);
        betRepo.saveAll(bets);
    }
}
