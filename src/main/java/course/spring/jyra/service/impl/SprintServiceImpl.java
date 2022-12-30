package course.spring.jyra.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import course.spring.jyra.dao.*;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.*;
import course.spring.jyra.service.SprintService;

@Service
public class SprintServiceImpl implements SprintService {
	private final SprintRepository sprintRepository;
	private final UserRepository userRepository;
	private final SprintResultRepository sprintResultRepository;
	private final ProjectRepository projectRepository;
	private final BoardRepository boardRepository;

	@Autowired
	public SprintServiceImpl(SprintRepository sprintRepository, UserRepository userRepository,
			SprintResultRepository sprintResultRepository, ProjectRepository projectRepository, BoardRepository boardRepository) {
		this.sprintRepository = sprintRepository;
		this.userRepository = userRepository;
		this.sprintResultRepository = sprintResultRepository;
		this.projectRepository = projectRepository;
		this.boardRepository = boardRepository;
	}

	@Override
	public List<Sprint> findAll() {
		return sprintRepository.findAll();
	}

	@Override
	public Sprint findById(Integer id) {
		return sprintRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with ID=%s not found.", id)));
	}

	@Override
	public Sprint findByTitle(String title) {
		return sprintRepository.findByTitle(title)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with title=%s not found.", title)));
	}

	@Override
	public Sprint create(Sprint sprint, Integer ownerId, Integer projectId, Integer boardId, Integer resultId) {
		sprint.setId(null);

		User user = userRepository.findById(ownerId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s could not be found", ownerId)));
		sprint.setOwner(user);

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Project with id=%s could not be found", projectId)));
		sprint.setProject(project);

		if (resultId != null) {
			SprintResult sprintResult = sprintResultRepository.findById(resultId)
					.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint result with id=%s could not be found", resultId)));
			sprint.setSprintResult(sprintResult);
		}

		sprint.setCreated(LocalDateTime.now());
		sprint.setModified(LocalDateTime.now());
		sprint.calculateDuration();
		return sprintRepository.save(sprint);
	}

	@Override
	public Sprint update(Sprint sprint, Integer boardId, Integer resultId) {
		Sprint oldSprint = findById(sprint.getId());
		sprint.setOwner(oldSprint.getOwner());
		sprint.setProject(oldSprint.getProject());
		sprint.calculateDuration();

		if (boardId != null) {
			Board board = boardRepository.findById(boardId)
					.orElseThrow(() -> new EntityNotFoundException(String.format("Board with id=%s could not be found", boardId)));
			sprint.setBoard(board);
		}

		if (resultId != null) {
			SprintResult sprintResult = sprintResultRepository.findById(resultId)
					.orElseThrow(() -> new EntityNotFoundException(String.format("Sprint result with id=%s could not be found", resultId)));
			sprint.setSprintResult(sprintResult);
		}
		sprint.setCreated(oldSprint.getCreated());
		sprint.setModified(LocalDateTime.now());
		return sprintRepository.save(sprint);
	}

	@Override
	public Sprint deleteById(Integer id) {
		Sprint oldSprint = findById(id);
		sprintRepository.deleteById(id);
		return oldSprint;
	}

	@Override
	public long count() {
		return sprintRepository.count();
	}
}
