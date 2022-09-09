package de.judgeman.EmailService;

import de.judgeman.EmailService.Exceptions.UsernameAlreadyExistsException;
import de.judgeman.EmailService.Services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EmailServiceApplication {

	public static void main(String[] args) throws UsernameAlreadyExistsException {
		ConfigurableApplicationContext context = SpringApplication.run(EmailServiceApplication.class, args);

		context.getBean(UserService.class).init();
	}
}
