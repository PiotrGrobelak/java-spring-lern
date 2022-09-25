package spring.demo.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.demo.model.Project;
import spring.demo.model.ProjectRepository;
import spring.demo.model.TaskGroup;
import spring.demo.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {


    @Override
    @Query("select distinct p from Project p join fetch p.steps")
        // hql method - similar do java syntax
    List<Project> findAll();


}
