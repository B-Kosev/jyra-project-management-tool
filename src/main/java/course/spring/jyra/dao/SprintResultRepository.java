package course.spring.jyra.dao;

import course.spring.jyra.model.SprintResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface SprintResultRepository extends CrudRepository<SprintResult, Integer> {
}
