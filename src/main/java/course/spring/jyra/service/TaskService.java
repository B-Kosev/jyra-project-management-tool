package course.spring.jyra.service;

import course.spring.jyra.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task findById(Integer id);

    Task findByTitle(String title);

    Task create(Task task);

    Task create(Task task, Integer projectId);

    Task update(Task task, Integer oldId, Integer projectId);

    Task update(Task task);

    Task deleteById(Integer id);

    Task deleteById(Integer id, Integer projectId);

    List<Task> findBySearch(String keywords);

    long count();
}
