package course.spring.jyra.service.impl;

import course.spring.jyra.dao.*;
import course.spring.jyra.exception.EntityNotFoundException;

import course.spring.jyra.model.*;

import course.spring.jyra.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final SprintRepository sprintRepository;
    private final ProjectResultRepository projectResultRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, BoardRepository boardRepository, SprintRepository sprintRepository, ProjectResultRepository projectResultRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.sprintRepository = sprintRepository;
        this.projectResultRepository = projectResultRepository;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(Integer id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Project with ID=%s not found.", id)));
    }

    @Override
    public Project findByTitle(String title) {
        return projectRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException(String.format("Project with title=%s not found.", title)));
    }

    @Override
    public Project create(Project project, Integer ownerId, Integer boardId, Integer activeSprintId, Integer projectResultId) {
        project.setId(null);
        project.setCreated(LocalDateTime.now());
        project.setModified(LocalDateTime.now());

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s could not be found", ownerId)));
        project.setOwner(user);

        if(boardId != null){
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Board with id=%s could not be found", boardId)));
            project.setBoard(board);
        }

        if(activeSprintId != null){
            Sprint sprint = sprintRepository.findById(activeSprintId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with id=%s could not be found", activeSprintId)));
            project.setActiveSprint(sprint);
        }

        if(projectResultId != null){
            ProjectResult projectResult = projectResultRepository.findById(projectResultId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Project result with id=%s could not be found", projectResultId)));
            project.setProjectResult(projectResult);
        }

        return projectRepository.save(project);
    }

    @Override
    public Project update(Project project, Integer boardId, Integer activeSprintId, Integer projectResultId) {
        Project oldProject = findById(project.getId());
        project.setCreated(oldProject.getCreated());
        project.setModified(LocalDateTime.now());
        project.setOwner(oldProject.getOwner());

        if(boardId != null){
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Board with id=%s could not be found", boardId)));
            project.setBoard(board);
        }

        if(activeSprintId != null){
            Sprint sprint = sprintRepository.findById(activeSprintId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with id=%s could not be found", activeSprintId)));
            project.setActiveSprint(sprint);
        }

        if(projectResultId != null){
            ProjectResult projectResult = projectResultRepository.findById(projectResultId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Project result with id=%s could not be found", projectResultId)));
            project.setProjectResult(projectResult);
        }
        return projectRepository.save(project);
    }

    @Override
    public Project deleteById(Integer id) {
        Project oldProject = findById(id);
        projectRepository.deleteById(id);
        return oldProject;
    }

    @Override
    public long count() {
        return projectRepository.count();
    }
}
