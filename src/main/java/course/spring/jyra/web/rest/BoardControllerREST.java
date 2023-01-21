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
import course.spring.jyra.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardControllerREST {
	private final BoardService boardService;

	@Autowired
	public BoardControllerREST(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping
	public List<Board> getBoards() {
		log.info("GET: All boards.");
		return boardService.findAll();
	}

	@GetMapping("/{boardId}")
	public Board getBoardById(@PathVariable Integer boardId) {
		log.info("GET: Board with ID={}", boardId);
		return boardService.findById(boardId);
	}

	@PostMapping
	public ResponseEntity<Board> addBoard(@RequestBody Board board, @RequestParam Integer projectId, @RequestParam Integer sprintId) {
		Board created = boardService.create(board, projectId, sprintId);
		log.info("POST: Board with ID={}", created.getId());
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{boardId}").buildAndExpand(created.getId()).toUri())
				.body(created);
	}

	@PutMapping("/{boardId}")
	public Board updateBoard(@PathVariable Integer boardId, @RequestBody Board board) {
		if (!boardId.equals(board.getId()))
			throw new InvalidClientDataException(
					String.format("Board ID %s from URL doesn't match ID %s in Request body", boardId, board.getId()));
		log.info("PUT: Board with ID={}", boardId);
		return boardService.update(board);
	}

	@DeleteMapping("/{boardId}")
	public Board deleteBoard(@PathVariable Integer boardId) {
		log.info("Delete: Board with ID={}", boardId);
		return boardService.deleteById(boardId);
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
