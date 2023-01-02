package course.spring.jyra.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import course.spring.jyra.dao.TaskRepository;
import course.spring.jyra.dao.TaskResultRepository;
import course.spring.jyra.dao.UserRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.ExistingEntityException;
import course.spring.jyra.model.Task;
import course.spring.jyra.model.TaskResult;
import course.spring.jyra.model.User;
import course.spring.jyra.service.TaskResultService;

@Service
public class TaskResultServiceImpl implements TaskResultService {
	private final TaskResultRepository taskResultRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	@Autowired
	public TaskResultServiceImpl(TaskResultRepository taskResultRepository, TaskRepository taskRepository, UserRepository userRepository) {
		this.taskResultRepository = taskResultRepository;
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<TaskResult> findAll() {
		return taskResultRepository.findAll();
	}

	@Override
	public TaskResult findById(Integer id) {
		return taskResultRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Task result with ID=%s not found.", id)));
	}

	@Override
	public TaskResult create(TaskResult taskResult, Integer taskId, Integer approverId) {
		Optional<TaskResult> result = taskResultRepository.findAll().stream().filter(tr -> tr.getTask().getId().equals(taskId)).findFirst();
		if (result.isPresent()) {
			throw new ExistingEntityException(String.format("There is existing task result for task with ID=%s.", taskId));
		}

		taskResult.setId(null);

		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Task with id=%s could not be found", taskId)));
		task.setTaskResult(taskResult);
		taskResult.setTask(task);

		User user = userRepository.findById(approverId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s could not be found", approverId)));
		taskResult.setVerifiedBy(user);

		taskResult.setCreated(LocalDateTime.now());
		taskResult.setModified(LocalDateTime.now());
		return taskResultRepository.save(taskResult);
	}

	@Override
	public TaskResult update(Integer taskId, TaskResult taskResult) {
		TaskResult oldTaskResult = findByTaskId(taskId);
		taskResult.setId(oldTaskResult.getId());
		taskResult.setTask(oldTaskResult.getTask());
		taskResult.setVerifiedBy(oldTaskResult.getVerifiedBy());
		taskResult.setCreated(oldTaskResult.getCreated());
		taskResult.setModified(LocalDateTime.now());
		return taskResultRepository.save(taskResult);
	}

	@Override
	public TaskResult deleteById(Integer id) {
		TaskResult oldTaskResult = findById(id);

		Task task = oldTaskResult.getTask();
		task.setTaskResult(null);

		taskResultRepository.deleteById(id);

		return oldTaskResult;
	}

	@Override
	public TaskResult findByTaskId(Integer id) {
		return taskResultRepository.findAll().stream().filter(taskResult -> taskResult.getTask().getId().equals(id)).findFirst()
				.orElseThrow(() -> new EntityNotFoundException(String.format("Task with ID=%s not found.", id)));
	}

	@Override
	public long count() {
		return taskResultRepository.count();
	}
}
