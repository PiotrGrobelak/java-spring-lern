package spring.demo.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    boolean existsById(Integer id);

//    void delete(Integer id);

    Page<Task> findAll(Pageable page);
    List<Task> findByDone(boolean done);
    Task save(Task entity);
}
