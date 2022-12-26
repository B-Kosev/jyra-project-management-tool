package course.spring.jyra.service;

import course.spring.jyra.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();

    Project findById(Integer id);

    Project findByTitle(String title);

    Project create(Project project);

    Project deleteById(Integer id);

    Project update(Project project, Integer oldId);

    Project update(Project project);

    List<Project> findBySearch(String keywords);

    long count();

}
