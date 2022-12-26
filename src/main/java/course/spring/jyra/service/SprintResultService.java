package course.spring.jyra.service;

import course.spring.jyra.model.SprintResult;

import java.util.List;

public interface SprintResultService {
    List<SprintResult> findAll();

    SprintResult findById(Integer id);

    SprintResult create(SprintResult sprintResult);

    SprintResult update(SprintResult sprintResult, Integer oldId);

    SprintResult update(SprintResult sprintResult);

    SprintResult deleteById(Integer id);

    SprintResult findBySprintId(Integer id);

    long count();
}
