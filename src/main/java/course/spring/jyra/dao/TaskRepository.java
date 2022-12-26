package course.spring.jyra.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
	Optional<Task> findByTitle(String title);
}
