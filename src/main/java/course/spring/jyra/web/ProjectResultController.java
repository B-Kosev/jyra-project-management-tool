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

import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/projectresults")
@Slf4j
public class ProjectResultController {
	private final ProjectResultService projectResultService;
	private final ProjectService projectService;
	private final SprintResultService sprintResultService;
	private final SprintService sprintService;
	private final UserService userService;
	private final HtmlService htmlService;

	@Autowired
	public ProjectResultController(ProjectResultService projectResultService, ProjectService projectService, ProjectService projectService1,
			SprintResultService sprintResultService, SprintService sprintService, UserService userService, HtmlService htmlService) {
		this.projectResultService = projectResultService;
		this.projectService = projectService1;
		this.sprintResultService = sprintResultService;
		this.sprintService = sprintService;
		this.userService = userService;
		this.htmlService = htmlService;
	}

	@GetMapping
	public String getProjectResults(Model model) {
		Map<ProjectResult, Project> map = new HashMap<>();
		projectResultService.findAll().forEach(projectResult -> map.put(projectResult, projectResult.getProject()));

		model.addAttribute("projectResults", projectResultService.findAll());
		model.addAttribute("map", map);

		log.debug("GET: Project results: {}", projectResultService.findAll());
		return "all-project-results";
	}

	@GetMapping("/{projectId}/project-result")
	public String getResultsByProjectId(Model model, @PathVariable("projectId") Integer projectId) {
		ProjectResult projectResult = projectResultService.findByProject(projectId);
		List<SprintResult> sprintResultsList = projectResult.getProject().getSprints().stream().map(Sprint::getSprintResult)
				.collect(Collectors.toList());
		Map<SprintResult, Sprint> map = new HashMap<>();
		sprintResultsList.forEach(sprintResult -> map.put(sprintResult, sprintResult.getSprint()));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User editor = userService.findByUsername(auth.getName());
		boolean canEditProjectResult = false;

		model.addAttribute("canEditProjectResult", true);
		model.addAttribute("projectResult", projectResult);
		model.addAttribute("project", projectResult.getProject());
		model.addAttribute("sprintResults", sprintResultsList);
		model.addAttribute("map", map);
		model.addAttribute("htmlService", htmlService);

		log.debug("GET: Result of Project with ID=%s: {}", projectId, projectResultService.findByProject(projectId));
		return "single-project-result";
	}

	@GetMapping("/create")
	public String getCreateProjectResult(Model model, @RequestParam Integer projectId) {
		Project project = projectService.findById(projectId);

		if (!model.containsAttribute("projectResult")) {
			model.addAttribute("projectResult", new ProjectResult());
		}
		model.addAttribute("request", "POST");
		model.addAttribute("project", project);
		return "form-project-result";
	}

	@PostMapping("/create")
	public String addProjectResult(@ModelAttribute ProjectResult projectResult, @RequestParam Integer projectId) {
		projectResultService.create(projectResult, projectId);
		log.debug("POST: Project result: {}", projectResult);
		return "redirect:/projectresults";
	}

	@GetMapping("/edit")
	public String getEditProjectResult(Model model, @RequestParam Integer projectResultId) {
		ProjectResult projectResult = projectResultService.findById(projectResultId);
		if (!model.containsAttribute("projectResult")) {
			model.addAttribute("projectResult", projectResult);
		}
		model.addAttribute("request", "PUT");
		return "form-project-result";
	}

	@DeleteMapping("/delete")
	public String deleteProjectResult(@RequestParam Integer projectResultId) {
		ProjectResult projectResult = projectResultService.findById(projectResultId);
		log.debug("DELETE: Project result: {}", projectResult);
		projectResultService.deleteById(projectResultId);
		return "redirect:/projectresults";
	}

	@PutMapping("/edit")
	public String updateProjectResult(@RequestParam Integer projectResultId, @ModelAttribute ProjectResult projectResult) {
		log.debug("UPDATE: Project result: {}", projectResult);
		projectResult.setId(projectResultId);
		projectResultService.update(projectResult.getProject().getId(), projectResult);
		return "redirect:/projectresults";
	}
}
