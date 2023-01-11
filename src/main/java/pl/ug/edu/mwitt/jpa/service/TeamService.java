package pl.ug.edu.mwitt.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.Team;
import pl.ug.edu.mwitt.jpa.repository.PersonRepository;
import pl.ug.edu.mwitt.jpa.repository.TeamRepository;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    final TeamRepository teamRepo;


    public TeamService(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public TeamRepository getRepo() {
        return teamRepo;
    }

    public void importMany(List<Team> teams) {
        System.out.println(teams);
        teamRepo.saveAll(teams);
    }
}
