package spring.demo.services;

import org.springframework.stereotype.Service;
import spring.demo.TaskConfigurationProperties;
import spring.demo.model.*;
import spring.demo.model.dto.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }


    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project toSave) {
        return repository.save((toSave));
    }

    public GroupReadModel createGroup(LocalDateTime deadLine, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup taskGroup = repository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(project.getSteps().stream()
                            .map(step -> new Task(
                                    step.getDescription(),
                                    deadLine.plusDays(step.getDaysToDeadline()))
                            ).collect(Collectors.toSet())
                    );
                    result.setProject(project);
                    return taskGroupRepository.save(result);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(taskGroup);
    }

}
