package course.spring.jyra.service;

import java.util.List;

import course.spring.jyra.model.Task;

public interface TaskService {
	List<Task> findAll();

	Task findById(Integer id);

	Task findByTitle(String title);

	Task create(Task task, Integer reporterId, Integer projectId, Integer sprintId);

	Task update(Task task, Integer sprintId);

	Task deleteById(Integer id);

	Task deleteById(Integer id, Integer projectId);

	long count();
}
