package spring.demo.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.demo.TaskConfigurationProperties;
import spring.demo.model.ProjectRepository;
import spring.demo.model.TaskGroupRepository;
import spring.demo.model.TaskRepository;

@Configuration
public class LogicConfiguration {
    
    @Bean
    ProjectService projectService(
        final ProjectRepository repository, 
        final TaskGroupRepository taskGroupRepository,
        final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(
          final TaskGroupRepository taskGroupRepository,
          final TaskRepository taskRepository
    ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
