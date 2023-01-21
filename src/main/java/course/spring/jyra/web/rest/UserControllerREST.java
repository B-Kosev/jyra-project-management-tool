package course.spring.jyra.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import course.spring.jyra.dao.ProjectRepository;
import course.spring.jyra.dao.SprintRepository;
import course.spring.jyra.dao.TaskRepository;
import course.spring.jyra.dao.TaskResultRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.InvalidClientDataException;
import course.spring.jyra.model.ErrorResponse;
import course.spring.jyra.model.User;
import course.spring.jyra.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserControllerREST {
	private final UserService userService;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;
	private final TaskRepository taskRepository;
	private final TaskResultRepository taskResultRepository;

	@Autowired
	public UserControllerREST(UserService userService, ProjectRepository projectRepository, SprintRepository sprintRepository,
			TaskRepository taskRepository, TaskResultRepository taskResultRepository) {
		this.userService = userService;
		this.projectRepository = projectRepository;
		this.sprintRepository = sprintRepository;
		this.taskRepository = taskRepository;
		this.taskResultRepository = taskResultRepository;
	}

	@GetMapping
	public List<User> getUsers() {
		log.info("GET: All users");
		return userService.findAll();
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Integer userId) {
		log.info("GET: User with ID={}", userId);
		return userService.findById(userId);
	}

	@PostMapping
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User created = userService.create(user);
		log.info("POST: User with ID={}", created.getId());
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{userId}").buildAndExpand(created.getId()).toUri())
				.body(created);
	}

	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Integer userId, @RequestBody User user) {
		if (!userId.equals(user.getId()))
			throw new InvalidClientDataException(
					String.format("User ID %s from URL doesn't match ID %s in Request body", userId, user.getId()));
		log.info("PUT: User with ID={}", userId);
		return userService.update(user);
	}

	@DeleteMapping("/{userId}")
	public User deleteUser(@PathVariable Integer userId) {
		User user = userService.findById(userId);
		projectRepository.deleteAll(user.getProjects());
		sprintRepository.deleteAll(user.getSprints());
		taskRepository.deleteAll(user.getTasks());
		taskResultRepository.deleteAll(user.getTaskResults());
		log.info("DELETE: User with ID={}", userId);
		return userService.deleteById(userId);
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
