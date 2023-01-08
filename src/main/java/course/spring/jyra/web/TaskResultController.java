package course.spring.jyra.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import course.spring.jyra.model.Task;
import course.spring.jyra.model.TaskResult;
import course.spring.jyra.model.TaskStatus;
import course.spring.jyra.model.User;
import course.spring.jyra.service.HtmlService;
import course.spring.jyra.service.TaskResultService;
import course.spring.jyra.service.TaskService;
import course.spring.jyra.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/taskresults")
@Slf4j
public class TaskResultController {
	private final TaskResultService taskResultService;
	private final TaskService taskService;
	private final UserService userService;
	private final HtmlService htmlService;

	@Autowired
	public TaskResultController(TaskResultService taskResultService, TaskService taskService, UserService userService,
			HtmlService htmlService) {
		this.taskResultService = taskResultService;
		this.taskService = taskService;
		this.userService = userService;
		this.htmlService = htmlService;
	}

	@GetMapping
	public String getTaskResult(Model model) {
		Map<TaskResult, Task> taskMap = new HashMap<>();
		Map<TaskResult, User> userMap = new HashMap<>();
		taskResultService.findAll().forEach(taskResult -> {
			taskMap.put(taskResult, taskResult.getTask());
			userMap.put(taskResult, taskResult.getVerifiedBy());
		});

		model.addAttribute("taskResults", taskResultService.findAll());
		model.addAttribute("taskMap", taskMap);
		model.addAttribute("userMap", userMap);

		log.debug("GET: Task results: {}", taskResultService.findAll());
		return "all-task-results";
	}

	@GetMapping("/{taskId}/task-result")
	public String getTaskResultByTaskId(Model model, @PathVariable("taskId") Integer id) {
		TaskResult taskResult = taskResultService.findByTaskId(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User editor = userService.findByUsername(auth.getName());
		boolean canEditTaskResult = false;

		// if (taskService.findById(id).getDevelopersAssignedIds().contains(editor.getId())
		// || taskService.findById(id).getAddedById().equals(editor.getId())) {
		// canEditTaskResult = true;
		// }

		model.addAttribute("canEditTaskResult", canEditTaskResult);
		model.addAttribute("taskResult", taskResult);
		model.addAttribute("approver", taskResult.getVerifiedBy());
		model.addAttribute("task", taskResult.getTask());
		model.addAttribute("htmlService", htmlService);

		log.debug("GET: Result of task with Id:%s {}", id, taskResultService.findAll());
		return "single-task-result";
	}

	@GetMapping("/create")
	public String getCreateTaskResult(Model model) {
		if (!model.containsAttribute("taskResult")) {
			model.addAttribute("taskResult", new TaskResult());
		}
		model.addAttribute("request", "POST");
		model.addAttribute("users", userService.findAll());
		return "form-task-result";
	}

	@PostMapping("/create")
	public String addTaskResult(@ModelAttribute TaskResult taskResult, @RequestParam Integer taskId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		taskResultService.create(taskResult, taskId, user.getId());

		// update task status through service in order to apply changes to board
		Task task = taskService.findById(taskId);
		task.setStatus(TaskStatus.DONE);
		taskService.update(task, task.getSprint() != null ? task.getSprint().getId() : null);

		log.debug("POST: Task result: {}", taskResult);
		return "redirect:/taskresults";
	}

	@GetMapping("/edit")
	public String getEditTaskResult(Model model, @RequestParam Integer taskResultId) {
		TaskResult taskResult = taskResultService.findById(taskResultId);
		if (!model.containsAttribute("taskResult")) {
			model.addAttribute("taskResult", taskResult);
		}
		model.addAttribute("request", "PUT");
		model.addAttribute("users", userService.findAll());
		return "form-task-result";
	}

	@DeleteMapping("/delete")
	public String deleteTaskResult(@RequestParam Integer taskResultId) {
		TaskResult taskResult = taskResultService.findById(taskResultId);

		// update task status through service in order to apply changes to board
		Task task = taskResult.getTask();
		task.setStatus(TaskStatus.IN_PROGRESS);
		taskService.update(task, task.getSprint().getId());

		log.debug("DELETE: Task result: {}", taskResult);
		taskResultService.deleteById(taskResultId);
		return "redirect:/taskresults";
	}

	@PutMapping("/edit")
	public String updateTaskResult(@RequestParam Integer taskResultId, @ModelAttribute TaskResult taskResult) {
		log.debug("UPDATE: Task result: {}", taskResult);
		taskResultService.update(taskResult.getTask().getId(), taskResult);
		return "redirect:/taskresults";
	}
}
