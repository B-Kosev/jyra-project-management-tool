package course.spring.jyra.web;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import course.spring.jyra.model.Project;
import course.spring.jyra.model.Role;
import course.spring.jyra.model.Task;
import course.spring.jyra.model.User;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
	private final TaskService taskService;
	private final UserService userService;
	private final SprintService sprintService;
	private final ProjectService projectService;
	private final HtmlService htmlService;

	@Autowired
	public TaskController(TaskService taskService, UserService userService, SprintService sprintService, ProjectService projectService,
			HtmlService htmlService) {
		this.taskService = taskService;
		this.userService = userService;
		this.sprintService = sprintService;
		this.projectService = projectService;
		this.htmlService = htmlService;
	}

	@GetMapping
	public String getTasks(Model model) {
		Map<Task, User> map = new HashMap<>();
		taskService.findAll().forEach(task -> map.put(task, task.getAddedBy()));

		model.addAttribute("tasks", taskService.findAll());
		model.addAttribute("map", map);
		log.debug("GET: Tasks: {}", taskService.findAll());
		return "all-tasks";
	}

	@GetMapping("/create")
	public String getCreateTask(Model model, @RequestParam Integer projectId) {
		Project project = projectService.findById(projectId);
		// if (project.getCurrentSprintId() != null) {
		// model.addAttribute("sprint", sprintService.findById(project.getCurrentSprintId()));
		// }

		if (!model.containsAttribute("task")) {
			model.addAttribute("task", new Task());
		}

		model.addAttribute("request", "POST");
		model.addAttribute("developers",
				userService.findAll().stream().filter(u -> u.getRole().equals(Role.DEVELOPER)).collect(Collectors.toList()));
		return "form-task";
	}

	@PostMapping("/create")
	public String addTask(@ModelAttribute Task task, @RequestParam Integer projectId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());

		taskService.create(task, user.getId(), projectId, null);
		log.debug("POST: Task: {}", task);
		return "redirect:/tasks";
	}

	@DeleteMapping("/delete")
	public String deleteTask(@RequestParam Integer taskId, @RequestParam Integer projectId) {
		Task task = taskService.findById(taskId);
		log.debug("DELETE: Task: {}", task);
		taskService.deleteById(taskId, projectId);
		return "redirect:/tasks";
	}

	@GetMapping("/{taskId}")
	public String getTaskById(Model model, @PathVariable("taskId") Integer id) {
		Task task = taskService.findById(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User editor = userService.findByUsername(auth.getName());

		model.addAttribute("task", task);
		model.addAttribute("reporter", task.getAddedBy());
		model.addAttribute("sprint", task.getSprint());

		log.debug("GET: Task with Id=%s : {}", id, taskService.findById(id));
		return "single-task";
	}

	@GetMapping("/edit")
	public String getEditTask(Model model, @RequestParam Integer taskId, @RequestParam Integer projectId) {
		Task task = taskService.findById(taskId);
		Project project = projectService.findById(projectId);
		if (!model.containsAttribute("task")) {
			model.addAttribute("task", task);
		}

		// if (project.getCurrentSprintId() != null) {
		// model.addAttribute("sprint", sprintService.findById(project.getCurrentSprintId()));
		// }

		model.addAttribute("request", "PUT");
		model.addAttribute("developers",
				userService.findAll().stream().filter(user -> user.getRole().equals(Role.DEVELOPER)).collect(Collectors.toList()));
		return "form-task";
	}

	@PutMapping("/edit")
	public String updateTask(@RequestParam Integer taskId, @RequestParam Integer projectId, @ModelAttribute Task task) {
		log.debug("UPDATE: Task: {}", task);
		task.setId(taskId);
		task.setProject(projectService.findById(projectId));
		taskService.update(task, null);
		return "redirect:/tasks";
	}

	// @GetMapping("/search")
	// public String getTasksBySearch(Model model, @RequestParam String keywords) {
	// Map<Task, User> map = new HashMap<>();
	// taskService.findBySearch(keywords).forEach(task -> map.put(task, userService.findById(task.getAddedById())));
	//
	// model.addAttribute("tasks", taskService.findBySearch(keywords));
	// model.addAttribute("map", map);
	//
	// log.debug("GET: Tasks by search: {}", taskService.findBySearch(keywords));
	// return "all-tasks";
	// }
}
