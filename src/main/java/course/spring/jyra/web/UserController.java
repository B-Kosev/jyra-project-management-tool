package course.spring.jyra.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import course.spring.jyra.exception.InvalidEntityException;
import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {
	private final UserService userService;
	private final TaskService taskService;
	private final TaskResultService taskResultService;
	private final ProjectService projectService;
	private final ProjectResultService projectResultService;
	private final HtmlService htmlService;

	@Autowired
	public UserController(UserService userService, TaskService taskService, TaskResultService taskResultService,
			ProjectService projectService, ProjectResultService projectResultService, HtmlService htmlService) {
		this.userService = userService;
		this.taskService = taskService;
		this.taskResultService = taskResultService;
		this.projectService = projectService;
		this.projectResultService = projectResultService;
		this.htmlService = htmlService;
	}

	@GetMapping
	public String getUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		log.debug("GET: Users: {}", userService.findAll());
		return "all-users";
	}

	@DeleteMapping("/delete")
	public String deleteUser(@RequestParam Integer userId) {
		User user = userService.findById(userId);
		log.debug("DELETE: User: {}", user);
		userService.deleteById(userId);
		return "redirect:/users";
	}

	@GetMapping("/{userId}")
	public String getUserById(Model model, @PathVariable("userId") Integer id) {
		User user = userService.findById(id);

		String userType = "";
		if (user.getRole().equals(Role.DEVELOPER)) {
			userType = "DEV";

			Set<Task> assignedTasks = user.getTasks();
			Set<TaskResult> completedTaskResults = user.getTaskResults();
			Map<Task, User> taskUserMapDev = new HashMap<>();
			Map<TaskResult, User> taskResultUserMapDev = new HashMap<>();
			Map<TaskResult, Task> taskResultTaskMapDev = new HashMap<>();
			assignedTasks.forEach(task -> taskUserMapDev.put(task, task.getAddedBy()));
			completedTaskResults.forEach(taskResult -> taskResultUserMapDev.put(taskResult, taskResult.getVerifiedBy()));
			completedTaskResults.forEach(taskResult -> taskResultTaskMapDev.put(taskResult, taskResult.getTask()));

			model.addAttribute("user", user);
			model.addAttribute("assignedTasks", assignedTasks);
			model.addAttribute("completedTaskResults", completedTaskResults);
			model.addAttribute("taskUserMapDev", taskUserMapDev);
			model.addAttribute("taskResultUserMapDev", taskResultUserMapDev);
			model.addAttribute("taskResultTaskMapDev", taskResultTaskMapDev);
		} else if (user.getRole().equals(Role.ADMIN)) {
			userType = "ADMIN";
			model.addAttribute("user", user);
		} else if (user.getRole().equals(Role.PRODUCT_OWNER)) {
			userType = "PO";

			Set<Project> projects = user.getProjects();
			Map<ProjectResult, Project> projectMapPo = new HashMap<>();

			model.addAttribute("user", user);
			model.addAttribute("projects", projects);
			model.addAttribute("projectMapPo", projectMapPo);
		} else {
			throw new InvalidEntityException(String.format("User with ID=%s is not one of the supported user types format.", id));
		}
		model.addAttribute("userType", userType);

		model.addAttribute("htmlService", htmlService);

		log.debug("GET: User with Id=%s : {}", id, userService.findById(id));
		return "single-user";
	}

	// @GetMapping("/edit")
	// public String getEditUser(Model model, @RequestParam String userId) {
	// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// User editor = userService.findByUsername(auth.getName());
	// boolean isAdmin = false;
	// boolean isProfileOwner = false;
	// if (editor instanceof Administrator) {
	// isAdmin = true;
	// }
	// if (editor.getId().equals(userId)) {
	// isProfileOwner = true;
	// }
	// User user = userService.findById(userId);
	// model.addAttribute("user", user);
	// model.addAttribute("isAdmin", isAdmin);
	// model.addAttribute("isProfileOwner", isProfileOwner);
	// return "form-user";
	// }

	@PutMapping("/edit")
	public String updateUser(@RequestParam Integer userId, @ModelAttribute User user) {
		log.debug("UPDATE: User: {}", user);
		user.setId(userId);
		userService.update(user);
		return "redirect:/users";
	}

	// @GetMapping("/search")
	// public String getUsersBySearch(Model model, @RequestParam String keywords) {
	// model.addAttribute("users", userService.findBySearch(keywords));
	//
	// log.debug("GET: Users by search: {}", userService.findBySearch(keywords));
	// return "all-users";
	// }
}
