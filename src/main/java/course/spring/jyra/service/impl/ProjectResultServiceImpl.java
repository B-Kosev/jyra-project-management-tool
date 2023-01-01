package course.spring.jyra.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import course.spring.jyra.dao.ProjectRepository;
import course.spring.jyra.dao.ProjectResultRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.ExistingEntityException;
import course.spring.jyra.model.Project;
import course.spring.jyra.model.ProjectResult;
import course.spring.jyra.service.ProjectResultService;

@Service
public class ProjectResultServiceImpl implements ProjectResultService {
	private final ProjectResultRepository projectResultRepository;
	private final ProjectRepository projectRepository;

	@Autowired
	public ProjectResultServiceImpl(ProjectResultRepository projectResultRepository, ProjectRepository projectRepository) {
		this.projectResultRepository = projectResultRepository;
		this.projectRepository = projectRepository;
	}

	@Override
	public List<ProjectResult> findAll() {
		return projectResultRepository.findAll();
	}

	@Override
	public ProjectResult findById(Integer id) {
		return projectResultRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Project result with ID=%s not found.", id)));
	}

	@Override
	public ProjectResult create(ProjectResult projectResult, Integer projectId) {
		Optional<ProjectResult> result = projectResultRepository.findAll().stream().filter(pr -> pr.getProject().getId().equals(projectId))
				.findFirst();
		if (result.isPresent()) {
			throw new ExistingEntityException(String.format("There is existing project result for project with ID=%s.", projectId));
		}

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", (projectId))));
		project.setProjectResult(projectResult);

		projectResult.setId(null);
		projectResult.setProject(project);
		projectResult.setCreated(LocalDateTime.now());
		projectResult.setModified(LocalDateTime.now());
		calculateDuration(projectResult, project);

		return projectResultRepository.save(projectResult);
	}

	@Override
	public ProjectResult update(ProjectResult projectResult, Integer oldId) {
		ProjectResult oldProjectResult = findById(oldId);

		projectResult.setId(oldProjectResult.getId());
		projectResult.setProject(oldProjectResult.getProject());
		projectResult.setCreated(oldProjectResult.getCreated());
		projectResult.setModified(LocalDateTime.now());

		return projectResultRepository.save(projectResult);
	}

	@Override
	public ProjectResult update(ProjectResult projectResult) {
		ProjectResult oldProjectResult = findById(projectResult.getId());
		projectResult.setProject(oldProjectResult.getProject());
		projectResult.setCreated(oldProjectResult.getCreated());
		projectResult.setModified(LocalDateTime.now());
		return projectResultRepository.save(projectResult);
	}

	@Override
	public ProjectResult deleteById(Integer id) {
		ProjectResult oldProjectResult = findById(id);
		Project project = oldProjectResult.getProject();
		project.setProjectResult(null);
		projectResultRepository.deleteById(id);
		return oldProjectResult;
	}

	@Override
	public ProjectResult findByProject(Integer id) {
		return projectResultRepository.findAll().stream().filter(projectResult -> projectResult.getProject().getId().equals(id)).findFirst()
				.orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", id)));
	}

	@Override
	public long count() {
		return projectResultRepository.count();
	}

	public void calculateDuration(ProjectResult projectResult, Project project) {
		projectResult.setDuration(DAYS.between(project.getStartDate(), projectResult.getEndDate()));
	}
}
