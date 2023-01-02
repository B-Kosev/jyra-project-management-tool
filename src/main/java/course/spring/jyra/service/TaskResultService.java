package course.spring.jyra.service;

import java.util.List;

import course.spring.jyra.model.TaskResult;

public interface TaskResultService {
	List<TaskResult> findAll();

	TaskResult findById(Integer id);

	TaskResult create(TaskResult taskResult, Integer taskId, Integer appeoverId);

	TaskResult update(Integer taskId, TaskResult taskResult);

	TaskResult deleteById(Integer id);

	TaskResult findByTaskId(Integer id);

	long count();
}
