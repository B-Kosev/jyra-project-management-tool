package course.spring.jyra.dao;

import course.spring.jyra.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    Optional<Sprint> findByTitle(String title);
}
