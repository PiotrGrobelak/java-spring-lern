package spring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import spring.demo.model.TaskRepository;

import javax.validation.Validator;

// @Import(TaskConfigurationProperties.class)
@SpringBootApplication
// @ComponentScan(basePackages = "db.migration") - for enter sprint to the folder and scan fetures to run
public class TodoExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoExampleApplication.class, args);
	}

	@Bean
	Validator validator(){
		// return repository.findById(1).map((task) -> new LocalValidatorFactoryBean()).orElse(null);
		return new LocalValidatorFactoryBean();
	}

}
