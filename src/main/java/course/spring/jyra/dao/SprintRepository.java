package course.spring.jyra.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
	Optional<Sprint> findByTitle(String title);
}
