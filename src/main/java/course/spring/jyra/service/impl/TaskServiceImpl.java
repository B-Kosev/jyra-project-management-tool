package course.spring.jyra.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import course.spring.jyra.dao.ProjectRepository;
import course.spring.jyra.dao.SprintRepository;
import course.spring.jyra.dao.TaskRepository;
import course.spring.jyra.dao.UserRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.Project;
import course.spring.jyra.model.Sprint;
import course.spring.jyra.model.Task;
import course.spring.jyra.model.User;
import course.spring.jyra.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository,
			SprintRepository sprintRepository) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.sprintRepository = sprintRepository;
	}

	@Autowired

	@Override
	public List<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Task findById(Integer id) {
		return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Task with ID=%s not found.", id)));
	}

	@Override
	public Task findByTitle(String title) {
		return taskRepository.findByTitle(title)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Task with title=%s not found.", title)));
	}

	@Override
	public Task create(Task task, Integer reporterId, Integer projectId, Integer sprintId) {
		task.setId(null);

		User user = userRepository.findById(reporterId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s could not be found", reporterId)));
		task.setAddedBy(user);

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Project with id=%s could not be found", projectId)));
		task.setProject(project);

		if (sprintId != null) {
			Sprint sprint = sprintRepository.findById(sprintId)
					.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with id=%s could not be found", sprintId)));
			task.setSprint(sprint);
		}

		task.setCreated(LocalDateTime.now());
		task.setModified(LocalDateTime.now());

		return taskRepository.save(task);
	}

	@Override
	public Task update(Task task, Integer sprintId) {
		Task oldTask = findById(task.getId());

		if (sprintId != null) {
			Sprint sprint = sprintRepository.findById(sprintId)
					.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with id=%s could not be found", sprintId)));
			task.setSprint(sprint);
		}

		task.setProject(oldTask.getProject());

		task.setModified(LocalDateTime.now());
		task.setAddedBy(oldTask.getAddedBy());
		return taskRepository.save(task);
	}

	@Override
	public Task deleteById(Integer id) {
		Task oldTask = findById(id);
		taskRepository.deleteById(id);
		return oldTask;
	}

	@Override
	public Task deleteById(Integer id, Integer projectId) {
		Task oldTask = findById(id);
		taskRepository.deleteById(id);
		return oldTask;
	}

	@Override
	public long count() {
		return taskRepository.count();
	}
}
