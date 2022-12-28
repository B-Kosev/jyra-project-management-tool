package course.spring.jyra.service.impl;



import course.spring.jyra.dao.SprintRepository;
import course.spring.jyra.dao.SprintResultRepository;
import course.spring.jyra.dao.TaskResultRepository;
import course.spring.jyra.exception.EntityNotFoundException;

import course.spring.jyra.model.Sprint;
import course.spring.jyra.model.SprintResult;
import course.spring.jyra.model.TaskResult;
import course.spring.jyra.service.SprintResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SprintResultServiceImpl implements SprintResultService {
    private final SprintResultRepository sprintResultRepository;
    private final TaskResultRepository taskResultRepository;
    private final SprintRepository sprintRepository;

    @Autowired
    public SprintResultServiceImpl(SprintResultRepository sprintResultRepository, TaskResultRepository taskResultRepository, SprintRepository sprintRepository) {
        this.sprintResultRepository = sprintResultRepository;
        this.taskResultRepository = taskResultRepository;
        this.sprintRepository = sprintRepository;
    }

    @Override
    public List<SprintResult> findAll() {
        return sprintResultRepository.findAll();
    }

    @Override
    public SprintResult findById(Integer id) {
        return sprintResultRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Sprint result with ID=%s not found.", id)));
    }

    @Override
    public SprintResult create(SprintResult sprintResult, Integer sprintId) {
        sprintResult.setId(null);
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with id=%s could not be found", sprintId)));
        sprintResult.setSprint(sprint);
        sprintResult.setCreated(LocalDateTime.now());
        sprintResult.setModified(LocalDateTime.now());
        sprintResult.setTeamVelocity(calculateTeamVelocity(sprintResult));

       return sprintResultRepository.save(sprintResult);
    }

    @Override
    public SprintResult update(SprintResult sprintResult) {
        SprintResult oldSprintResult = findById(sprintResult.getId());
        sprintResult.setCreated(oldSprintResult.getCreated());
        sprintResult.setModified(LocalDateTime.now());
        sprintResult.setTeamVelocity(calculateTeamVelocity(sprintResult));
        return sprintResultRepository.save(sprintResult);
    }

    @Override
    public SprintResult deleteById(Integer id) {
        SprintResult oldSprintResult = findById(id);
        sprintResultRepository.deleteById(id);

        return oldSprintResult;
    }

    @Override
    public SprintResult findBySprintId(Integer id) {
        return sprintResultRepository.findAll().stream().filter(sprintResult -> sprintResult.getSprint().getId().equals(id)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with ID=%s not found.", id)));
    }

    private int calculateTeamVelocity(SprintResult sprintResult) {
        return sprintResult.getSprint().getTasks().stream().map(task -> taskResultRepository.findById(task.getTaskResult().getId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task result with ID=%s not found.", task.getTaskResult().getId())))).mapToInt(TaskResult::getActualEffort).sum();
    }

    @Override
    public long count() {
        return sprintResultRepository.count();
    }
}
