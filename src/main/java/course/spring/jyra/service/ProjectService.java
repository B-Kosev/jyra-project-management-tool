package course.spring.jyra.service;

import course.spring.jyra.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();

    Project findById(Integer id);

    Project findByTitle(String title);

    Project create(Project project, Integer ownerId, Integer boardId, Integer activeSprintId, Integer projectResultId);

    Project deleteById(Integer id);

    Project update(Project project, Integer boardId, Integer activeSprintId, Integer projectResultId);

    long count();

}
