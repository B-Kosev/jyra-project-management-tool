package course.spring.jyra.service;

import course.spring.jyra.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task findById(Integer id);

    Task findByTitle(String title);

    Task create(Task task);

    Task update(Task task);

    Task deleteById(Integer id);

    Task deleteById(Integer id, Integer projectId);

    long count();
}
