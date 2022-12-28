package course.spring.jyra.service.impl;

import course.spring.jyra.dao.TaskRepository;
import course.spring.jyra.dao.TaskResultRepository;
import course.spring.jyra.dao.UserRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.Task;
import course.spring.jyra.model.TaskResult;
import course.spring.jyra.model.User;
import course.spring.jyra.service.TaskResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskResultServiceImpl implements TaskResultService {
    private final TaskResultRepository taskResultRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskResultServiceImpl(TaskResultRepository taskResultRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.taskResultRepository = taskResultRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskResult> findAll() {
        return taskResultRepository.findAll();
    }

    @Override
    public TaskResult findById(Integer id) {
        return taskResultRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Task result with ID=%s not found.", id)));
    }

    @Override
    public TaskResult create(TaskResult taskResult, Integer taskId, Integer approverId) {
        taskResult.setId(null);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task with id=%s could not be found", taskId)));
        taskResult.setTask(task);

        User user = userRepository.findById(approverId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s could not be found", approverId)));
        taskResult.setVerifiedBy(user);

        taskResult.setCreated(LocalDateTime.now());
        taskResult.setModified(LocalDateTime.now());
        return taskResultRepository.save(taskResult);
    }

    @Override
    public TaskResult update(TaskResult taskResult) {
        TaskResult oldTaskResult = findById(taskResult.getId());
        taskResult.setCreated(oldTaskResult.getCreated());
        taskResult.setModified(LocalDateTime.now());
        return taskResultRepository.save(taskResult);
    }

    @Override
    public TaskResult deleteById(Integer id) {
        TaskResult oldTaskResult = findById(id);
        taskResultRepository.deleteById(id);

        return oldTaskResult;
    }

    @Override
    public TaskResult findByTaskId(Integer id) {
        return taskResultRepository.findAll().stream().filter(taskResult -> taskResult.getTask().getId().equals(id)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task with ID=%s not found.", id)));
    }

    @Override
    public long count() {
        return taskResultRepository.count();
    }
}
