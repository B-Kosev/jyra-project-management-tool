//package course.spring.jyra.web;
//
//import course.spring.jyra.exception.EntityNotFoundException;
//import course.spring.jyra.model.*;
//import course.spring.jyra.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/sprints")
//@Slf4j
//public class SprintController {
//    private final SprintService sprintService;
//    private final UserService userService;
//    private final TaskService taskService;
//    private final ProjectService projectService;
//    private final BoardService boardService;
//
//    @Autowired
//    public SprintController(SprintService sprintService, UserService userService, TaskService taskService, ProjectService projectService, BoardService boardService) {
//        this.sprintService = sprintService;
//        this.userService = userService;
//        this.taskService = taskService;
//        this.projectService = projectService;
//        this.boardService = boardService;
//    }
//
//    @GetMapping
//    public String getSprints(Model model) {
//        Map<Sprint, User> map = new HashMap<>();
//        sprintService.findAll().forEach(sprint -> map.put(sprint, userService.findById(sprint.getOwnerId())));
//
//        model.addAttribute("sprints", sprintService.findAll());
//        model.addAttribute("map", map);
//
//        log.debug("GET: Sprints: {}", sprintService.findAll());
//        return "all-sprints";
//    }
//
//    //    TODO: Finish when SP-63 is done
//    @GetMapping("/create")
//    public String getCreateSprint(Model model, @RequestParam String projectId) {
//        Project project = projectService.findById(projectId);
//
//        if (!model.containsAttribute("sprint")) {
//            model.addAttribute("sprint", new Sprint());
//        }
//
//        model.addAttribute("request", "POST");
//        model.addAttribute("project", project);
//        model.addAttribute("developers", userService.findAll().stream().filter(user -> user.getRoles().contains(Role.DEVELOPER)).collect(Collectors.toList()));
//        model.addAttribute("tasks", project.getTasksBacklogIds().stream().map(taskService::findById).filter(task -> !task.getStatus().equals(TaskStatus.DONE)).collect(Collectors.toList()));
//        return "form-sprint";
//    }
//
//    @PostMapping("/create")
//    public String addSprint(@ModelAttribute Sprint sprint) {
//        Board board = Board.builder().projectId(sprint.getProjectId()).build();
//        boardService.create(board);
//        sprintService.create(sprint);
//        log.debug("POST: Sprint: {}", sprint);
//        return "redirect:/sprints";
//    }
//
//    @DeleteMapping("/delete")
//    public String deleteSprint(@RequestParam String sprintId) {
//        Sprint sprint = sprintService.findById(sprintId);
//
//        Board board = boardService.findAll().stream().filter(b -> b.getProjectId().equals(sprint.getProjectId())).findFirst().orElseThrow(() -> new EntityNotFoundException(String.format("Board for project with ID=%s not found.", sprint.getProjectId())));
//        boardService.deleteById(board.getId());
//
//        log.debug("DELETE: Sprint: {}", sprint);
//        sprintService.deleteById(sprintId);
//        return "redirect:/sprints";
//    }
//
//    @GetMapping("/{sprintId}")
//    public String getSprintById(Model model, @PathVariable("sprintId") String id) {
//        model.addAttribute("sprint", sprintService.findById(id));
//        log.debug("GET: Sprint with Id=%s : {}", id, sprintService.findById(id));
//        //TODO:should redirect to other page
//        return "redirect:/sprints";
//    }
//
//    @GetMapping("/edit")
//    public String getEditSprint(Model model, @RequestParam String sprintId) {
//        Sprint sprint = sprintService.findById(sprintId);
//        if (!model.containsAttribute("sprint")) {
//            model.addAttribute("sprint", sprint);
//        }
//        model.addAttribute("request", "PUT");
//        model.addAttribute("owners", userService.findAll().stream().filter(user -> user.getRoles().contains(Role.PRODUCT_OWNER)).collect(Collectors.toList()));
//        model.addAttribute("developers", userService.findAll().stream().filter(user -> user.getRoles().contains(Role.DEVELOPER)).collect(Collectors.toList()));
//        model.addAttribute("tasks", taskService.findAll().stream().filter(task -> task.getSprintId() == null).collect(Collectors.toList()));
//        return "form-sprint";
//    }
//
//    @PutMapping("/edit")
//    public String updateSprint(@RequestParam String sprintId, @ModelAttribute Sprint sprint) {
//        log.debug("UPDATE: Sprint: {}", sprint);
//        sprintService.update(sprint, sprintId);
//        return "redirect:/sprints";
//    }
//
//    @GetMapping("/search")
//    public String getSprintsBySearch(Model model, @RequestParam String keywords) {
//        Map<Sprint, User> map = new HashMap<>();
//        sprintService.findBySearch(keywords).forEach(sprint -> map.put(sprint, userService.findById(sprint.getOwnerId())));
//
//        model.addAttribute("sprints", sprintService.findBySearch(keywords));
//        model.addAttribute("map", map);
//
//        log.debug("GET: Tasks by search: {}", sprintService.findBySearch(keywords));
//        return "all-sprints";
//    }
//}
