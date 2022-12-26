package course.spring.jyra.service;

import course.spring.jyra.model.ProjectResult;

import java.util.List;

public interface ProjectResultService {
    List<ProjectResult> findAll();

    ProjectResult findById(Integer id);

    ProjectResult create(ProjectResult projectResult);

    ProjectResult update(ProjectResult projectResult, Integer oldId);

    ProjectResult update(ProjectResult projectResult);

    ProjectResult deleteById(Integer id);

    ProjectResult findByProject(Integer id);

    long count();
}
