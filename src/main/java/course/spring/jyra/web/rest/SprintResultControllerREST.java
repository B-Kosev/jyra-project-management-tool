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
import course.spring.jyra.model.SprintResult;
import course.spring.jyra.service.BoardService;
import course.spring.jyra.service.SprintResultService;
import course.spring.jyra.service.SprintService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sprints")
@Slf4j
public class SprintResultControllerREST {
	private final SprintResultService sprintResultService;
	private final SprintService sprintService;
	private final BoardService boardService;

	@Autowired
	public SprintResultControllerREST(SprintResultService sprintResultService, SprintService sprintService, BoardService boardService) {
		this.sprintResultService = sprintResultService;
		this.sprintService = sprintService;
		this.boardService = boardService;
	}

	@GetMapping("/sprint-results")
	public List<SprintResult> getSprintResults() {
		log.info("GET: All sprint results");
		return sprintResultService.findAll();
	}

	@GetMapping("/{sprintId}/sprint-result")
	public SprintResult getResultsByProjectId(@PathVariable Integer sprintId) {
		log.info("GET: Sprint result for sprint with ID={}", sprintId);
		return sprintResultService.findBySprintId(sprintId);
	}

	@PostMapping("/{sprintId}/sprint-result")
	public ResponseEntity<SprintResult> addSprintResult(@PathVariable Integer sprintId, @RequestBody SprintResult sprintResult) {
		SprintResult created = sprintResultService.create(sprintResult, sprintId);
		log.info("POST: Sprint result for sprint with ID={}", sprintId);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{projectId}")
				.buildAndExpand(created.getSprint().getId()).toUri()).body(created);
	}

	@PutMapping("/{sprintId}/sprint-result")
	public SprintResult updateSprintResult(@PathVariable Integer sprintId, @RequestBody SprintResult sprintResult) {
		log.info("PUT: Sprint result for sprint with ID={}", sprintId);
		return sprintResultService.update(sprintId, sprintResult);
	}

	@DeleteMapping("/{sprintId}/sprint-result")
	public SprintResult deleteSprintResult(@PathVariable Integer sprintId) {
		Integer deletedId = sprintService.findById(sprintId).getSprintResult().getId();

		SprintResult sprintResult = sprintResultService.findBySprintId(sprintId);

		Board board = Board.builder().project(sprintService.findById(sprintResult.getSprint().getId()).getProject())
				.sprint(sprintResult.getSprint()).build();
		boardService.create(board, sprintService.findById(sprintId).getProject().getId(), sprintId);
		log.info("DELETE: Sprint result for sprint with ID={}", sprintId);
		return sprintResultService.deleteById(deletedId);
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
