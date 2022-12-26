package course.spring.jyra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.TaskResult;

public interface TaskResultRepository extends JpaRepository<TaskResult, Integer> {
}
