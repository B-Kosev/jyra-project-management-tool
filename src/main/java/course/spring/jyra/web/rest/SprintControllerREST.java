package course.spring.jyra.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.InvalidClientDataException;
import course.spring.jyra.model.Board;
import course.spring.jyra.model.ErrorResponse;
import course.spring.jyra.model.Sprint;
import course.spring.jyra.service.BoardService;
import course.spring.jyra.service.SprintService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sprints")
@Slf4j
public class SprintControllerREST {
	private final SprintService sprintService;
	private final BoardService boardService;

	@Autowired
	public SprintControllerREST(SprintService sprintService, BoardService boardService) {
		this.sprintService = sprintService;
		this.boardService = boardService;
	}

	@GetMapping
	public List<Sprint> getSprints() {
		log.info("GET: All sprints");
		return sprintService.findAll();
	}

	@GetMapping("/{sprintId}")
	public Sprint getSprintById(@PathVariable Integer sprintId) {
		log.info("GET: Sprint with ID={}", sprintId);
		return sprintService.findById(sprintId);
	}

	@PostMapping
	public ResponseEntity<Sprint> addSprint(@RequestBody Sprint sprint, @RequestParam Integer ownerId, @RequestParam Integer projectId,
			@RequestParam(required = false) Integer resultId) {
		Sprint created = sprintService.create(sprint, ownerId, projectId, null, resultId);
		Board board = boardService.create(Board.builder().build(), projectId, sprint.getId());
		created = sprintService.update(created, board.getId(),
				created.getSprintResult() == null ? null : created.getSprintResult().getId());
		log.info("GET: Sprint with ID={}", created.getId());
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{sprintId}").buildAndExpand(created.getId()).toUri())
				.body(created);
	}

	@PutMapping("/{sprintId}")
	public Sprint updateSprint(@PathVariable Integer sprintId, @RequestBody Sprint sprint, @RequestParam(required = false) Integer boardId,
			@RequestParam(required = false) Integer resultId) {
		if (!sprintId.equals(sprint.getId()))
			throw new InvalidClientDataException(
					String.format("Sprint ID %s from URL doesn't match ID %s in Request body", sprintId, sprint.getId()));
		log.info("PUT: Sprint with ID={}", sprintId);
		return sprintService.update(sprint, boardId, resultId);
	}

	@DeleteMapping("/{sprintId}")
	public Sprint deleteSprint(@PathVariable Integer sprintId) {
		log.info("DELETE: Sprint with ID={}", sprintId);
		return sprintService.deleteById(sprintId);
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
