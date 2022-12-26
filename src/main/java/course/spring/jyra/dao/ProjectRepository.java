package course.spring.jyra.dao;

import course.spring.jyra.model.Project;
import course.spring.jyra.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByTitle(String title);
}
