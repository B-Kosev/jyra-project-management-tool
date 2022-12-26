package course.spring.jyra.web.rest;


import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.exception.InvalidClientDataException;
import course.spring.jyra.model.Board;
import course.spring.jyra.model.ErrorResponse;
import course.spring.jyra.model.Sprint;
import course.spring.jyra.service.BoardService;
import course.spring.jyra.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
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
        return sprintService.findAll();
    }

    @GetMapping("/{sprintId}")
    public Sprint getSprintById(@PathVariable Integer sprintId) {
        return sprintService.findById(sprintId);
    }

    @PostMapping
    public ResponseEntity<Sprint> addSprint(@RequestBody Sprint sprint) {
        Board board = Board.builder().project(sprint.getProject()).build();
        boardService.create(board);
        Sprint created = sprintService.create(sprint);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .pathSegment("{sprintId}").buildAndExpand(created.getId()).toUri()).body(created);
    }

    @PutMapping("/{sprintId}")
    public Sprint updateSprint(@PathVariable Integer sprintId, @RequestBody Sprint sprint) {
        if (!sprintId.equals(sprint.getId()))
            throw new InvalidClientDataException(String.format("Sprint ID %s from URL doesn't match ID %s in Request body", sprintId, sprint.getId()));
        return sprintService.update(sprint);
    }

    @DeleteMapping("/{sprintId}")
    public Sprint deleteSprint(@PathVariable Integer sprintId) {
        return sprintService.deleteById(sprintId);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException entityNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), entityNotFoundException.getMessage(), entityNotFoundException.toString()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInvalidClientData(InvalidClientDataException invalidClientDataException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), invalidClientDataException.getMessage(), invalidClientDataException.toString()));
    }
}
