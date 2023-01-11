package pl.ug.edu.mwitt.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ug.edu.mwitt.jpa.domain.Match;
import pl.ug.edu.mwitt.jpa.domain.Team;
import pl.ug.edu.mwitt.jpa.repository.MatchRepository;
import pl.ug.edu.mwitt.jpa.repository.TeamRepository;

import java.util.List;

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
        System.out.println(matches);
        matchRepo.saveAll(matches);
    }
}
