package course.spring.jyra.model;

import course.spring.jyra.service.ProjectService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductOwner extends User {
    @Builder.Default
    private List<String> projectsIds = new ArrayList<>();

    @Builder.Default
    private List<String> completedProjectResultsIds = new ArrayList<>();

    public String printProjects(ProjectService projectService) {
        StringBuilder stringBuilder = new StringBuilder();
        this.getProjectsIds().forEach(projectId ->
                stringBuilder.append(String.format("%s , ", projectService.findById(projectId).getTitle())));
        return stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
    }
}
