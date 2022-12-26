package course.spring.jyra.dao;

import course.spring.jyra.model.ProjectResult;
import course.spring.jyra.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectResultRepository extends CrudRepository<ProjectResult, Integer> {
}
