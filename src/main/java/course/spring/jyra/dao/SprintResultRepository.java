package course.spring.jyra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.SprintResult;

public interface SprintResultRepository extends JpaRepository<SprintResult, Integer> {
}
