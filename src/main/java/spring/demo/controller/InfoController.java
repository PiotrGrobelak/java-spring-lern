package spring.demo.controller;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.demo.TaskConfigurationProperties;

@RestController
class InfoController {

    private final DataSourceProperties dataSource;
    private final TaskConfigurationProperties allowMultipleTasksFromTemplate;

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties allowMultipleTasksFromTemplate) {
        this.dataSource = dataSource;
        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
    }

    @GetMapping("/info/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    Boolean allowMultipleTasksFromTemplate() {
     return allowMultipleTasksFromTemplate.getTemplate().isAllowMultipleTasks();
    }
}
