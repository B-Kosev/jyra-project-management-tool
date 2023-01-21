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
import course.spring.jyra.model.Project;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/projects")
@Slf4j
public class ProjectControllerREST {
	private final ProjectService projectService;
	private final ProjectResultService projectResultService;
	private final SprintService sprintService;
	private final BoardService boardService;
	private final TaskService taskService;
	private final TaskResultService taskResultService;
	private final SprintResultService sprintResultService;

	@Autowired
	public ProjectControllerREST(ProjectService projectService, ProjectResultService projectResultService, SprintService sprintService,
			BoardService boardService, TaskService taskService, TaskResultService taskResultService,
			SprintResultService sprintResultService) {
		this.projectService = projectService;
		this.projectResultService = projectResultService;
		this.sprintService = sprintService;
		this.boardService = boardService;
		this.taskService = taskService;
		this.taskResultService = taskResultService;
		this.sprintResultService = sprintResultService;
	}

	@GetMapping
	public List<Project> getProjects() {
		log.info("GET: All projects");
		return projectService.findAll();
	}

	@GetMapping("/{projectId}")
	public Project getProjectById(@PathVariable Integer projectId) {
		log.info("GET: Project with ID={}", projectId);
		return projectService.findById(projectId);
	}

	@PostMapping
	public ResponseEntity<Project> addProject(@RequestBody Project project, @RequestParam Integer ownerId,
			@RequestParam(required = false) Integer boardId, @RequestParam(required = false) Integer activeSprintId,
			@RequestParam(required = false) Integer projectResultId) {
		Project created = projectService.create(project, ownerId, boardId, activeSprintId, projectResultId);
		log.info("POST: Project with ID={}", created.getId());
		return ResponseEntity
				.created(
						ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{projectId}").buildAndExpand(created.getId()).toUri())
				.body(created);
	}

	@PutMapping("/{projectId}")
	public Project updateProject(@PathVariable Integer projectId, @RequestBody Project project,
			@RequestParam(required = false) Integer boardId, @RequestParam(required = false) Integer activeSprintId,
			@RequestParam(required = false) Integer projectResultId) {
		if (!projectId.equals(project.getId()))
			throw new InvalidClientDataException(
					String.format("Project ID %s from URL doesn't match ID %s in Request body", projectId, project.getId()));
		log.info("PUT: Project with ID={}", projectId);
		return projectService.update(project, boardId, activeSprintId, projectResultId);
	}

	@DeleteMapping("/{projectId}")
	public Project deleteProject(@PathVariable Integer projectId) {
		log.info("DELETE: Project with ID={}", projectId);
		return projectService.deleteById(projectId);
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
