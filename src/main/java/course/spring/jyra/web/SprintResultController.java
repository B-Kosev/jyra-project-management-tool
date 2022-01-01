package course.spring.jyra.web;

import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sprintresults")
@Slf4j
public class SprintResultController {
    private final SprintResultService sprintResultService;
    private final SprintService sprintService;
    private final TaskResultService taskResultService;
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public SprintResultController(SprintResultService sprintResultService, SprintService sprintService, TaskResultService taskResultService, TaskService taskService, UserService userService) {
        this.sprintResultService = sprintResultService;
        this.sprintService = sprintService;
        this.taskResultService = taskResultService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public String getSprintResult(Model model) {
        Map<SprintResult, Sprint> map = new HashMap<>();
        sprintResultService.findAll().forEach(sprintResult -> map.put(sprintResult, sprintService.findById(sprintResult.getSprintId())));

        model.addAttribute("sprintResults", sprintResultService.findAll());
        model.addAttribute("map", map);

        log.debug("GET: Sprint results: {}", sprintResultService.findAll());
        return "all-sprint-results";
    }


    @GetMapping("/{sprintId}/sprint-result")
    public String getSprintResultByProjectId(Model model, @PathVariable String sprintId) {
        SprintResult sprintResult = sprintResultService.findBySprintId(sprintId);
        List<TaskResult> taskResultsList = sprintResult.getTaskResultsIds().stream().map(taskResultService::findById).collect(Collectors.toList());
        Map<TaskResult, Task> taskMap = new HashMap<>();
        Map<TaskResult, User> userMap = new HashMap<>();
        taskResultsList.forEach(taskResult -> {
            taskMap.put(taskResult, taskService.findById(taskResult.getTaskId()));
            userMap.put(taskResult, userService.findById(taskResult.getVerifiedById()));
        });

        model.addAttribute("sprintResult", sprintResult);
        model.addAttribute("sprint", sprintService.findById(sprintResult.getSprintId()));
        model.addAttribute("taskResults", taskResultsList);
        model.addAttribute("taskMap", taskMap);
        model.addAttribute("userMap", userMap);

        log.debug("GET: Sprint result: {}", sprintResultService.findBySprintId(sprintId));
        return "single-sprint-result";
    }

    @PostMapping
    public String addSprintResult(@ModelAttribute("sprintResult") SprintResult sprintResult) {
        sprintResultService.create(sprintResult);
        log.debug("POST: Sprint result: {}", sprintResult);
        return "redirect:/sprintsresults";
    }

    @DeleteMapping
    public String deleteProjectResult(@RequestParam("delete") String id) {
        SprintResult sprintResult = sprintResultService.findById(id);
        log.debug("DELETE: Sprint result: {}", sprintResult);
        sprintResultService.deleteById(id);
        return "redirect:/sprintresults";
    }

    @PutMapping
    public String updateSprintResult(@RequestParam("update") String id) {
        SprintResult sprintResult = sprintResultService.findById(id);
        log.debug("UPDATE: Sprint result: {}", sprintResult);
        sprintResultService.update(sprintResult);
        return "redirect:/sprintresults";
    }
}
