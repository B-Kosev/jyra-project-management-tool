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
import course.spring.jyra.model.TaskResult;
import course.spring.jyra.model.TaskStatus;
import course.spring.jyra.service.TaskResultService;
import course.spring.jyra.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskResultControllerREST {
	private final TaskResultService taskResultService;
	private final TaskService taskService;

	@Autowired
	public TaskResultControllerREST(TaskResultService taskResultService, TaskService taskService) {
		this.taskResultService = taskResultService;
		this.taskService = taskService;
	}

	@GetMapping("/task-results")
	public List<TaskResult> getTaskResults() {
		return taskResultService.findAll();
	}

	@GetMapping("/{taskId}/task-result")
	public TaskResult getResultsByTaskId(@PathVariable Integer taskId) {
		return taskResultService.findById(taskId);
	}

	@PostMapping("/{taskId}/task-result")
	public ResponseEntity<TaskResult> addTaskResult(@PathVariable Integer taskId, @RequestBody TaskResult taskResult,
			@RequestParam Integer approverId) {
		TaskResult created = taskResultService.create(taskResult, taskId, approverId);

		Task task = taskService.findById(taskResult.getTask().getId());
		task.setStatus(TaskStatus.DONE);
		taskService.update(task, task.getSprint().getId());

		return ResponseEntity.created(
				ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{taskId}").buildAndExpand(created.getTask().getId()).toUri())
				.body(created);
	}

	@PutMapping("/{taskId}/task-result")
	public TaskResult updateTask(@PathVariable Integer taskId, @RequestBody TaskResult taskResult) {
		return taskResultService.update(taskId, taskResult);
	}

	@DeleteMapping("/{taskId}/task-result")
	public TaskResult deleteTaskResult(@PathVariable Integer taskId) {
		Integer deletedId = taskService.findById(taskId).getTaskResult().getId();

		Task task = taskService.findById(taskId);
		task.setStatus(TaskStatus.IN_PROGRESS);
		taskService.update(task, task.getSprint().getId());

		return taskResultService.deleteById(deletedId);
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
