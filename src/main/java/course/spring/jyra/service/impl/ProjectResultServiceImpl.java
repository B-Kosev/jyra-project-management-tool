package course.spring.jyra.service.impl;

import course.spring.jyra.dao.ProjectRepository;
import course.spring.jyra.dao.ProjectResultRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.ExistingEntityException;
import course.spring.jyra.model.Project;
import course.spring.jyra.model.ProjectResult;
import course.spring.jyra.service.ProjectResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

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
        return projectResultRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Project result with ID=%s not found.", id)));
    }

    @Override
    public ProjectResult create(ProjectResult projectResult, Integer projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", (projectId))));

        if (project.getActiveSprint() != null) {
            throw new ExistingEntityException("Project result cannot be created because not all sprints in the project are completed.");
        }

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
        projectResultRepository.deleteById(id);
        return oldProjectResult;
    }

    @Override
    public ProjectResult findByProject(Integer id) {
        return projectResultRepository.findAll().stream().filter(projectResult -> projectResult.getProject().getId().equals(id)).findFirst().orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", id)));
    }

    @Override
    public long count() {
        return projectResultRepository.count();
    }

    public void calculateDuration(ProjectResult projectResult, Project project) {
        projectResult.setDuration(DAYS.between(project.getStartDate(), projectResult.getEndDate()));
    }
}
