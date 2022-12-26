package course.spring.jyra.dao;

import course.spring.jyra.model.Board;
import course.spring.jyra.model.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BoardRepository extends CrudRepository<Board, Integer> {
}
