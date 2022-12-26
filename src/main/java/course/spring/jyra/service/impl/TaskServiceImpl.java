package course.spring.jyra.service.impl;

import course.spring.jyra.dao.TaskRepository;
import course.spring.jyra.dao.UserRepository;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.Task;
import course.spring.jyra.model.User;
import course.spring.jyra.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Integer id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Task with ID=%s not found.", id)));
    }

    @Override
    public Task findByTitle(String title) {
        return taskRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException(String.format("Task with title=%s not found.", title)));
    }

    @Override
    public Task create(Task task) {
        task.setId(null);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new EntityNotFoundException(String.format("User with username=%s could not be found", auth.getName())));
            task.setAddedBy(user);
        }

        task.setCreated(LocalDateTime.now());
        task.setModified(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        Task oldTask = findById(task.getId());
        task.setModified(LocalDateTime.now());
        task.setAddedBy(oldTask.getAddedBy());
        return taskRepository.save(task);
    }

    @Override
    public Task deleteById(Integer id) {
        Task oldTask = findById(id);
        taskRepository.deleteById(id);
        return oldTask;
    }

    @Override
    public Task deleteById(Integer id, Integer projectId) {
        Task oldTask = findById(id);
        taskRepository.deleteById(id);
        return oldTask;
    }

    @Override
    public long count() {
        return taskRepository.count();
    }
}
