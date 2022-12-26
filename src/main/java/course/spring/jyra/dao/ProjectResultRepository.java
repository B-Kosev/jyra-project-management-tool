package course.spring.jyra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.ProjectResult;

public interface ProjectResultRepository extends JpaRepository<ProjectResult, Integer> {
}
