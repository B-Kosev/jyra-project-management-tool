package course.spring.jyra.service;

import java.util.List;

import course.spring.jyra.model.SprintResult;

public interface SprintResultService {
	List<SprintResult> findAll();

	SprintResult findById(Integer id);

	SprintResult create(SprintResult sprintResult, Integer sprintId);

	SprintResult update(Integer sprintId, SprintResult sprintResult);

	SprintResult deleteById(Integer id);

	SprintResult findBySprintId(Integer id);

	long count();
}
