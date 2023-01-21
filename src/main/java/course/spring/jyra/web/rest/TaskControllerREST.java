package course.spring.jyra.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.InvalidClientDataException;
import course.spring.jyra.model.ErrorResponse;
import course.spring.jyra.model.Task;
import course.spring.jyra.service.TaskService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskControllerREST {
	private final TaskService taskService;

	@Autowired
	public TaskControllerREST(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public List<Task> getTask() {
		log.info("GET: All tasks");
		return taskService.findAll();
	}

	@GetMapping("/{taskId}")
	public Task getTaskById(@PathVariable Integer taskId) {
		log.info("GET: Task with ID={}", taskId);
		return taskService.findById(taskId);
	}

	@PostMapping
	public ResponseEntity<Task> addTask(@RequestBody Task task, @RequestParam Integer reporterId, @RequestParam Integer projectId,
			@RequestParam(required = false) Integer sprintId) {
		Task created = taskService.create(task, reporterId, projectId, sprintId);
		log.info("GET: Task with ID={}", created.getId());
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{taskId}").buildAndExpand(created.getId()).toUri())
				.body(created);
	}

	@PutMapping("/{taskId}")
	public Task updateTask(@PathVariable Integer taskId, @RequestBody Task task, @RequestParam(required = false) Integer sprintId) {
		if (!taskId.equals(task.getId()))
			throw new InvalidClientDataException(
					String.format("Task ID %s from URL doesn't match ID %s in Request body", taskId, task.getId()));
		log.info("PUT: Task with ID={}", taskId);
		return taskService.update(task, sprintId);
	}

	@DeleteMapping("/{taskId}")
	public Task deleteTask(@PathVariable Integer taskId) {
		log.info("DELETE: Task with ID={}", taskId);
		return taskService.deleteById(taskId);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException entityNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ErrorResponse(HttpStatus.NOT_FOUND.value(), entityNotFoundException.getMessage(), entityNotFoundException.toString()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleInvalidClientData(InvalidClientDataException invalidClientDataException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
				invalidClientDataException.getMessage(), invalidClientDataException.toString()));
	}
}
