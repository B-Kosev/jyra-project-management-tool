package course.spring.jyra.service.impl;

import course.spring.jyra.dao.ProjectRepository;
import course.spring.jyra.dao.ProjectResultRepository;
import course.spring.jyra.dao.UserRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.ExistingEntityException;
import course.spring.jyra.model.ProductOwner;
import course.spring.jyra.model.Project;
import course.spring.jyra.model.ProjectResult;
import course.spring.jyra.model.User;
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
    private final UserRepository userRepository;

    @Autowired
    public ProjectResultServiceImpl(ProjectResultRepository projectResultRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectResultRepository = projectResultRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
    public ProjectResult create(ProjectResult projectResult) {
        Integer projectId = projectResult.getProjectId();
        Optional<ProjectResult> projectMatch = projectResultRepository.findAll().stream().filter(projectResult1 -> projectResult1.getProjectId().equals(projectId)).findAny();

        if (projectMatch.isPresent()) {
            throw new EntityNotFoundException(String.format("There is a result created for project with ID=%s", projectId));
        }

        Project project = projectRepository.findById(projectResult.getProjectId()).orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", (projectResult.getProjectId()))));

        if (project.getCurrentSprintId() != null) {
            throw new ExistingEntityException("Project result cannot be created because not all sprints in the project are completed.");
        }

        projectResult.setId(null);
        project.getPreviousSprintResultsIds().forEach(sprintResultId -> projectResult.getSprintResultListIds().add(sprintResultId));
        projectResult.setCreated(LocalDateTime.now());
        projectResult.setModified(LocalDateTime.now());
        calculateDuration(projectResult, project);

        ProjectResult updated = projectResultRepository.save(projectResult);

        project.setProjectResultId(updated.getId());
        projectRepository.save(project);

        ProductOwner po = (ProductOwner) userRepository.findById(project.getOwnerId()).orElseThrow(() -> new EntityNotFoundException(String.format("User with ID=%s not found.", (project.getOwnerId()))));
        po.getCompletedProjectResultsIds().add(updated.getId());
        userRepository.save(po);

        return updated;
    }

    @Override
    public ProjectResult update(ProjectResult projectResult, Integer oldId) {
        ProjectResult oldProjectResult = findById(oldId);

        projectResult.setId(oldProjectResult.getId());
        projectResult.setProjectId(oldProjectResult.getProjectId());
        projectResult.setCreated(oldProjectResult.getCreated());
        projectResult.setModified(LocalDateTime.now());

        return projectResultRepository.save(projectResult);
    }

    @Override
    public ProjectResult update(ProjectResult projectResult) {
        ProjectResult oldProjectResult = findById(projectResult.getId());
        projectResult.setCreated(oldProjectResult.getCreated());
        projectResult.setModified(LocalDateTime.now());
        return projectResultRepository.save(projectResult);
    }

    @Override
    public ProjectResult deleteById(Integer id) {
        ProjectResult oldProjectResult = findById(id);
        projectResultRepository.deleteById(id);

        Project project = projectRepository.findById(oldProjectResult.getProjectId()).orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", (oldProjectResult.getProjectId()))));
        project.setProjectResultId(null);
        projectRepository.save(project);

        User po = userRepository.findById(project.getOwnerId()).orElseThrow(() -> new EntityNotFoundException(String.format("User with ID=%s not found.", (project.getOwnerId()))));
        po.getCompletedProjectResultsIds().remove(oldProjectResult.getId());
        userRepository.save(po);

        return oldProjectResult;
    }

    @Override
    public ProjectResult findByProject(Integer id) {
        return projectResultRepository.findAll().stream().filter(projectResult -> projectResult.getProjectId().equals(id)).findFirst().orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", id)));
    }

    @Override
    public long count() {
        return projectResultRepository.count();
    }

    public void calculateDuration(ProjectResult projectResult, Project project) {
        projectResult.setDuration(DAYS.between(project.getStartDate(), projectResult.getEndDate()));
    }
}
