package spring.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
//    @RestResource(path = "done", rel = "done")
//    List<Task> findByDone(@Param("state") boolean done);

}
