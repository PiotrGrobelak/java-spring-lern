package spring.demo.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.demo.TaskConfigurationProperties;
import spring.demo.model.ProjectRepository;
import spring.demo.model.TaskGroupRepository;

@Configuration
public class LogicConfiguration {
    
    @Bean
    ProjectService service(
        final ProjectRepository repository, 
        final TaskGroupRepository taskGroupRepository,
        final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, config);
    }
}
