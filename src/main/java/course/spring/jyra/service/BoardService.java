package course.spring.jyra.service;

import course.spring.jyra.model.Board;
import course.spring.jyra.model.Project;

import java.util.List;

public interface BoardService {
    List<Board> findAll();

    Board findById(Integer id);

    Board findByProjectId(Integer projectId);

    Board create(Board board);

    Board deleteById(Integer id);

    Board update(Board board);

    long count();
}
