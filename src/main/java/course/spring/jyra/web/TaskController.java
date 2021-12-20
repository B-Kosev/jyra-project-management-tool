package course.spring.jyra.web;

import course.spring.jyra.model.Sprint;
import course.spring.jyra.model.Task;
import course.spring.jyra.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        log.debug("GET: Tasks: {}", taskService.findAll());
        return "tasks";
    }

    @PostMapping
    public String addTask(@ModelAttribute("task") Task task) {
        taskService.create(task);
        log.debug("POST: Task: {}", task);
        return "redirect:/tasks";
    }

    @DeleteMapping
    public String deleteProject(@RequestParam("delete") String id) {
        Task task = taskService.findById(id);
        log.debug("DELETE: Task: {}", task);
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{taskId}")
    public String getTaskById(Model model, @PathVariable("taskId") String id) {
        model.addAttribute("task", taskService.findById(id));
        log.debug("GET: Task with Id=%s : {}", id, taskService.findById(id));
        return "redirect:/tasks"; //should redirect to other page
    }

    @PutMapping
    public String updateSprint(@RequestParam("update") String id) {
        Task task = taskService.findById(id);
        log.debug("UPDATE: Task: {}", task);
        taskService.update(task);
        return "redirect:/tasks";
    }
}
