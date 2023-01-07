package course.spring.jyra.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sprints")
@Slf4j
public class SprintController {
	private final SprintService sprintService;
	private final UserService userService;
	private final TaskService taskService;
	private final ProjectService projectService;
	private final BoardService boardService;

	@Autowired
	public SprintController(SprintService sprintService, UserService userService, TaskService taskService, ProjectService projectService,
			BoardService boardService) {
		this.sprintService = sprintService;
		this.userService = userService;
		this.taskService = taskService;
		this.projectService = projectService;
		this.boardService = boardService;
	}

	@GetMapping
	public String getSprints(Model model) {
		Map<Sprint, User> map = new HashMap<>();
		sprintService.findAll().forEach(sprint -> map.put(sprint, sprint.getOwner()));

		model.addAttribute("sprints", sprintService.findAll());
		model.addAttribute("map", map);

		log.debug("GET: Sprints: {}", sprintService.findAll());
		return "all-sprints";
	}

	// TODO: Finish when SP-63 is done
	@GetMapping("/create")
	public String getCreateSprint(Model model, @RequestParam Integer projectId) {
		Project project = projectService.findById(projectId);

		if (!model.containsAttribute("sprint")) {
			model.addAttribute("sprint", new Sprint());
		}

		model.addAttribute("request", "POST");
		// model.addAttribute("project", project);
		model.addAttribute("developers",
				userService.findAll().stream().filter(user -> user.getRole().equals(Role.DEVELOPER)).collect(Collectors.toList()));
		List<Task> tasks = project.getTasks().stream().filter(task -> !task.getStatus().equals(TaskStatus.DONE))
				.collect(Collectors.toList());
		log.info("Tasks: " + tasks);
		tasks.forEach(task -> System.out.println(task.toString()));
		model.addAttribute("tasks", tasks);
		return "form-sprint";
	}

	@PostMapping("/create")
	public String addSprint(@ModelAttribute Sprint sprint, @RequestParam Integer projectId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		Sprint created = sprintService.create(sprint, user.getId(), projectId, null, null);
		Board board = boardService.create(Board.builder().build(), projectId, sprint.getId());
		sprintService.update(created, board.getId(), created.getSprintResult() == null ? null : created.getSprintResult().getId());
		log.debug("POST: Sprint: {}", sprint);
		return "redirect:/sprints";
	}

	@DeleteMapping("/delete")
	public String deleteSprint(@RequestParam Integer sprintId) {
		Sprint sprint = sprintService.findById(sprintId);

		Board board = boardService.findAll().stream().filter(b -> b.getProject().getId().equals(sprint.getProject().getId())).findFirst()
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Board for project with ID=%s not found.", sprint.getProject().getId())));
		boardService.deleteById(board.getId());

		log.debug("DELETE: Sprint: {}", sprint);
		sprintService.deleteById(sprintId);
		return "redirect:/sprints";
	}

	@GetMapping("/{sprintId}")
	public String getSprintById(Model model, @PathVariable("sprintId") Integer id) {
		model.addAttribute("sprint", sprintService.findById(id));
		log.debug("GET: Sprint with Id=%s : {}", id, sprintService.findById(id));
		// TODO:should redirect to other page
		return "redirect:/sprints";
	}

	@GetMapping("/edit")
	public String getEditSprint(Model model, @RequestParam Integer sprintId) {
		Sprint sprint = sprintService.findById(sprintId);
		if (!model.containsAttribute("sprint")) {
			model.addAttribute("sprint", sprint);
		}
		model.addAttribute("request", "PUT");
		model.addAttribute("owners",
				userService.findAll().stream().filter(user -> user.getRole().equals(Role.PRODUCT_OWNER)).collect(Collectors.toList()));
		model.addAttribute("developers",
				userService.findAll().stream().filter(user -> user.getRole().equals(Role.DEVELOPER)).collect(Collectors.toList()));
		model.addAttribute("tasks",
				taskService.findAll().stream().filter(task -> task.getSprint().getId() == null).collect(Collectors.toList()));
		return "form-sprint";
	}

	@PutMapping("/edit")
	public String updateSprint(@RequestParam Integer sprintId, @ModelAttribute Sprint sprint) {
		log.debug("UPDATE: Sprint: {}", sprint);
		sprint.setId(sprintId);
		sprintService.update(sprint, sprint.getBoard().getId(), sprint.getSprintResult().getId());
		return "redirect:/sprints";
	}

	// @GetMapping("/search")
	// public String getSprintsBySearch(Model model, @RequestParam String keywords) {
	// Map<Sprint, User> map = new HashMap<>();
	// sprintService.findBySearch(keywords).forEach(sprint -> map.put(sprint, userService.findById(sprint.getOwnerId())));
	//
	// model.addAttribute("sprints", sprintService.findBySearch(keywords));
	// model.addAttribute("map", map);
	//
	// log.debug("GET: Tasks by search: {}", sprintService.findBySearch(keywords));
	// return "all-sprints";
	// }
}
