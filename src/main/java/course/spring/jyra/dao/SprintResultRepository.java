package course.spring.jyra.dao;

import course.spring.jyra.model.SprintResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface SprintResultRepository extends JpaRepository<SprintResult, Integer> {
}
