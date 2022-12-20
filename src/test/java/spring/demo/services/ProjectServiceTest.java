package spring.demo.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.demo.TaskConfigurationProperties;
import spring.demo.model.*;
import spring.demo.model.dto.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exist")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExist_throwsIllegalStateException() {
        //given flow
        TaskGroupRepository mockGroupRepository = groupRepository(true);
        // &&
        TaskConfigurationProperties mockConfig = getTaskConfigurationProperties(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        // when flow
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then flow
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");

//        assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500)); only for example code line

    }

    @Test
    @DisplayName("should throw IllegalStateException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        //given flow
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        TaskConfigurationProperties mockConfig = getTaskConfigurationProperties(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        // when flow
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then flow
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");

//        assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500)); only for example code line

    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and no groups and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExist_throwsIllegalStateException() {
        //given flow

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository mockGroupRepository = groupRepository(false);

        TaskConfigurationProperties mockConfig = getTaskConfigurationProperties(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);

        // when flow
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then flow
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");

//        assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500)); only for example code line

    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_exisitingProject_create_and_saveGroup() {

        var today = LocalDate.now().atStartOfDay();
        var project = projectWith("desc", Set.of(-1, -2));
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroup = inMemoryGroupRepository();
        int conutBeforeCall = inMemoryGroup.count();
        // and
        TaskConfigurationProperties mockConfig = getTaskConfigurationProperties(true);

        // under test
        var toTest = new ProjectService(mockRepository, inMemoryGroup, mockConfig);


        // then

        GroupReadModel result = toTest.createGroup(today, 1);


        assertThat(result).hasFieldOrPropertyWithValue("description", "desc");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
//        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("desc"));

        assertThat(conutBeforeCall + 1).isEqualTo(inMemoryGroup.count());


    }

    private Project projectWith(String description, Set<Integer> daysToDeadline) {

        var result = mock(Project.class);

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("temp desc");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());

        when(result.getDescription()).thenReturn(description);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }


    private static TaskGroupRepository groupRepository(boolean t) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(t);
        return mockGroupRepository;
    }

    private static TaskConfigurationProperties getTaskConfigurationProperties(boolean t) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(t);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                map.put(index, entity);
            } else {
                map.put(entity.getId(), entity);
            }
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }

    }
}