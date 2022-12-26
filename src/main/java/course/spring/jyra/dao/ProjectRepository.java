package course.spring.jyra.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
	Optional<Project> findByTitle(String title);
}
