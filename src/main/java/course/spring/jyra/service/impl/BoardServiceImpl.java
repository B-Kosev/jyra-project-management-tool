package course.spring.jyra.service.impl;

import course.spring.jyra.dao.BoardRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.Board;
import course.spring.jyra.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Board findById(Integer id) {
        return boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Board with ID=%s not found.", id)));
    }

    @Override
    public Board findByProjectId(Integer projectId) {
        return boardRepository.findAll().stream().filter(board -> board.getProject().getId().equals(projectId)).findFirst().orElseThrow(() -> new EntityNotFoundException(String.format("Board with project ID=%s not found.", projectId)));
    }

    @Override
    public Board create(Board board) {
        board.setId(null);
        board.setCreated(LocalDateTime.now());
        board.setModified(LocalDateTime.now());
        return boardRepository.save(board);
    }

    @Override
    public Board deleteById(Integer id) {
        Board oldBoard = findById(id);
        boardRepository.deleteById(id);
        return oldBoard;
    }

    @Override
    public Board update(Board board) {
        Board oldBoard = findById(board.getId());
        oldBoard.setCreated(oldBoard.getCreated());
        oldBoard.setModified(LocalDateTime.now());
        return boardRepository.save(board);
    }

    @Override
    public long count() {
        return boardRepository.count();
    }
}
