package spring.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.demo.model.Task;
import spring.demo.model.TaskRepository;

@Configuration
class TestConfiguration {
    @Bean
    TaskRepository testRepo(){
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

             private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);


            @Override
            public List<Task> findAll() {
                logger.info("Run test configuration");
                // TODO Auto-generated method stub
                return new ArrayList<>(tasks.values());
            }
        
            @Override
            public Page<Task> findAll(Pageable page) {
                // TODO Auto-generated method stub
                return null;
            }
        
            @Override
            public boolean existsById(Integer id) {
                // TODO Auto-generated method stub
                return tasks.containsKey(id);
            }
        
            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                // TODO Auto-generated method stub
                return false;
            }
        
            @Override
            public List<Task> findByDone(boolean done) {
                // TODO Auto-generated method stub
                return null;
            }
        
            @Override
            public Optional<Task> findById(Integer id) {
                // TODO Auto-generated method stub
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(final Task entity) {
                // TODO Auto-generated method stub
                return tasks.put(tasks.size() + 1, entity);
            }
        };
    };
};