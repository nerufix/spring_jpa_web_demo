package pl.ug.edu.mwitt.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
@SpringBootApplication
public class JpaApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(JpaApplication.class, args);
	}
}
