package pl.nikowis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VocabularyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(VocabularyApplication.class, args);
		context.getBean(DatabaseInitializer.class).populate();
	}
}
