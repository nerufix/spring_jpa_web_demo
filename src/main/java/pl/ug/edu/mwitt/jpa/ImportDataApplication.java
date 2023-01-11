package pl.ug.edu.mwitt.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import pl.ug.edu.mwitt.jpa.domain.Match;
import pl.ug.edu.mwitt.jpa.domain.Person;
import pl.ug.edu.mwitt.jpa.domain.Team;
import pl.ug.edu.mwitt.jpa.service.MatchService;
import pl.ug.edu.mwitt.jpa.service.PersonService;
import pl.ug.edu.mwitt.jpa.service.TeamService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ImportResource({"classpath:personData.xml", "classpath:teamData.xml", "classpath:matchData.xml"})
public class ImportDataApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ImportDataApplication.class, args);
		List<String> beanNames = List.of(ctx.getBeanDefinitionNames());
		PersonService ps = ctx.getBean(PersonService.class);
		TeamService ts = ctx.getBean(TeamService.class);
		MatchService ms = ctx.getBean(MatchService.class);
		List<Person> persons = new ArrayList<>();
		List<Team> teams = new ArrayList<>();
		List<Match> matches = new ArrayList<>();
		for (int i = 1; i < 300; i++) {
			if (beanNames.contains(String.valueOf(i))) {
				Object bean = ctx.getBean(String.valueOf(i));
				if (bean instanceof Team) {
					teams.add((Team) bean);
				}
				else if (bean instanceof Person) {
					persons.add((Person) bean);
				}
				else if (bean instanceof Match) {
					matches.add((Match) bean);
				}
			}
		}
		for (Person person : persons) {
			if (person.getTeam()!=null && person.getTeam().getId()>100) {
				person.getTeam().setId(person.getTeam().getId()-100);
			}

		}
		ts.importMany(teams);
		ps.importMany(persons);
		ms.importMany(matches);

	}
}
