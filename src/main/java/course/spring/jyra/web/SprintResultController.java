package course.spring.jyra.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sprintresults")
@Slf4j
public class SprintResultController {
	private final SprintResultService sprintResultService;
	private final SprintService sprintService;
	private final TaskResultService taskResultService;
	private final TaskService taskService;
	private final UserService userService;
	private final BoardService boardService;
	private final HtmlService htmlService;

	@Autowired
	public SprintResultController(SprintResultService sprintResultService, SprintService sprintService, TaskResultService taskResultService,
			TaskService taskService, UserService userService, BoardService boardService, HtmlService htmlService) {
		this.sprintResultService = sprintResultService;
		this.sprintService = sprintService;
		this.taskResultService = taskResultService;
		this.taskService = taskService;
		this.userService = userService;
		this.boardService = boardService;
		this.htmlService = htmlService;
	}

	@GetMapping
	public String getSprintResult(Model model) {
		Map<SprintResult, Sprint> map = new HashMap<>();
		sprintResultService.findAll().forEach(sprintResult -> map.put(sprintResult, sprintResult.getSprint()));

		model.addAttribute("sprintResults", sprintResultService.findAll());
		model.addAttribute("map", map);

		log.info("GET: Sprint results: {}", sprintResultService.findAll());
		return "all-sprint-results";
	}

	@GetMapping("/{sprintId}/sprint-result")
	public String getSprintResultBySprintId(Model model, @PathVariable Integer sprintId) {
		SprintResult sprintResult = sprintResultService.findBySprintId(sprintId);
		List<TaskResult> taskResultsList = sprintResult.getSprint().getTasks().stream().map(Task::getId)
				.map(taskResultService::findByTaskId).collect(Collectors.toList());
		Map<TaskResult, Task> taskMap = new HashMap<>();
		Map<TaskResult, User> userMap = new HashMap<>();
		taskResultsList.forEach(taskResult -> {
			taskMap.put(taskResult, taskResult.getTask());
			userMap.put(taskResult, taskResult.getVerifiedBy());
		});

		model.addAttribute("sprintResult", sprintResult);
		model.addAttribute("sprint", sprintResult.getSprint());
		model.addAttribute("taskResults", taskResultsList);
		model.addAttribute("taskMap", taskMap);
		model.addAttribute("userMap", userMap);
		model.addAttribute("htmlService", htmlService);

		log.info("GET: Sprint result: {}", sprintResultService.findBySprintId(sprintId));
		return "single-sprint-result";
	}

	@GetMapping("/create")
	public String getCreateSprintResult(Model model) {
		if (!model.containsAttribute("sprintResult")) {
			model.addAttribute("sprintResult", new SprintResult());
		}
		model.addAttribute("request", "POST");
		return "form-sprint-result";
	}

	@PostMapping("/create")
	public String addSprintResult(@ModelAttribute SprintResult sprintResult, @RequestParam Integer sprintId) {
		sprintResultService.create(sprintResult, sprintId);

		log.info("POST: Sprint result: {}", sprintResult);
		return "redirect:/sprintresults";
	}

	@GetMapping("/edit")
	public String getEditSprintResult(Model model, @RequestParam Integer sprintResultId) {
		SprintResult sprintResult = sprintResultService.findById(sprintResultId);
		if (!model.containsAttribute("sprintResult")) {
			model.addAttribute("sprintResult", sprintResult);
		}
		model.addAttribute("request", "PUT");

		return "form-sprint-result";
	}

	@PutMapping("/edit")
	public String updateSprintResult(@RequestParam Integer sprintResultId, @ModelAttribute SprintResult sprintResult) {
		log.info("UPDATE: Sprint result: {}", sprintResult);
		sprintResult.setId(sprintResultId);
		sprintResultService.update(sprintResult.getId(), sprintResult);
		return "redirect:/sprintresults";
	}

	@DeleteMapping("/delete")
	public String deleteSprintResult(@RequestParam Integer sprintResultId) {
		SprintResult sprintResult = sprintResultService.findById(sprintResultId);
		log.info("DELETE: Sprint result: {}", sprintResult);

		sprintResultService.deleteById(sprintResultId);

		return "redirect:/sprintresults";
	}
}
