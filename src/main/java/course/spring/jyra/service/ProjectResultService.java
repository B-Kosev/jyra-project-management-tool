package course.spring.jyra.service;

import java.util.List;

import course.spring.jyra.model.ProjectResult;

public interface ProjectResultService {
	List<ProjectResult> findAll();

	ProjectResult findById(Integer id);

	ProjectResult create(ProjectResult projectResult, Integer projectId);

	ProjectResult update(Integer projectId, ProjectResult projectResult);

	ProjectResult deleteById(Integer id);

	ProjectResult findByProject(Integer id);

	long count();
}
