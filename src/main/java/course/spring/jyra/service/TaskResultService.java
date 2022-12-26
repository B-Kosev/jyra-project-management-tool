package course.spring.jyra.service;

import course.spring.jyra.model.TaskResult;

import java.util.List;

public interface TaskResultService {
    List<TaskResult> findAll();

    TaskResult findById(Integer id);

    TaskResult create(TaskResult taskResult);

    TaskResult update(TaskResult taskResult, Integer oldId);

    TaskResult update(TaskResult taskResult);

    TaskResult deleteById(Integer id);

    TaskResult findByTaskId(Integer id);

    long count();
}
