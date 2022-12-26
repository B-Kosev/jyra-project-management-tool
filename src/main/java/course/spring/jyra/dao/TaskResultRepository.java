package course.spring.jyra.dao;

import course.spring.jyra.model.TaskResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface TaskResultRepository extends CrudRepository<TaskResult, Integer> {
}
