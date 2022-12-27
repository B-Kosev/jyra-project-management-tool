package course.spring.jyra.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import course.spring.jyra.model.*;
import course.spring.jyra.service.*;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {
	private final UserService userService;
	private final TaskService taskService;
	private final SprintService sprintService;
	private final ProjectService projectService;
	private final ProjectResultService projectResultService;
	private final SprintResultService sprintResultService;
	private final TaskResultService taskResultService;
	private final BoardService boardService;

	@Autowired
	public DataInitializer(UserService userService, TaskService taskService, SprintService sprintService, ProjectService projectService,
			ProjectResultService projectResultService, SprintResultService sprintResultService, TaskResultService taskResultService,
			BoardService boardService) {
		this.userService = userService;
		this.taskService = taskService;
		this.sprintService = sprintService;
		this.projectService = projectService;
		this.projectResultService = projectResultService;
		this.sprintResultService = sprintResultService;
		this.taskResultService = taskResultService;
		this.boardService = boardService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		User owner = User.builder().firstName("Ivaylo").lastName("Panayotov").email("ivaka@example.com").username("ivaka")
				.password("Ivaka123456@").role(Role.PRODUCT_OWNER).status(UserStatus.ACTIVE).imageUrl("any url").contacts("no contacts")
				.build();
		userService.create(owner);

		User developer = User.builder().firstName("Bogdan").lastName("Kosev").email("bogi4a@example.com").username("bogi4a")
				.password("Bogi4a1234@").role(Role.DEVELOPER).status(UserStatus.ACTIVE).imageUrl("any url").contacts("no contacts").build();
		userService.create(developer);

		User admin = User.builder().firstName("Ivan").lastName("Todorov").email("vankata@example.com").username("vankata")
				.password("Vankata1234@").role(Role.ADMIN).status(UserStatus.ACTIVE).imageUrl("any url").contacts("no contacts").build();
		userService.create(admin);

		Project project = Project.builder().title("IMC").description("Latest and greatest").owner(owner).tags("tag1").build();
		projectService.create(project);

		ProjectResult projectResult = ProjectResult.builder().project(project).duration(14).resultsDescription("actually very good project")
				.build();
		projectResultService.create(projectResult);

		Sprint sprint = Sprint.builder().title("My first sprint").duration(14).project(project).owner(owner).build();
		sprintService.create(sprint);

		SprintResult sprintResult = SprintResult.builder().sprint(sprint).resultsDescription("the best sprint").build();
		sprintResultService.create(sprintResult);

		Task task = Task.builder().title("Fix the code").taskType(TaskType.TASK).estimatedEffort(3).status(TaskStatus.TO_DO)
				.description("Fix everything").tags("no tags").build();
		taskService.create(task, developer.getId(), project.getId(), null);

		TaskResult taskResult = TaskResult.builder().task(task).actualEffort(3).resultsDescription("it was very hard").verifiedBy(owner)
				.build();
		taskResultService.create(taskResult);

		Board board = Board.builder().project(project).sprint(sprint).build();
		boardService.create(board);
	}
}
