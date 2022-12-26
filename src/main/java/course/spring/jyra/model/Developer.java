package course.spring.jyra.model;

import course.spring.jyra.service.TaskResultService;
import course.spring.jyra.service.TaskService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Developer extends User {
    @Column
    @Builder.Default
    private List<String> assignedTasksIds = new ArrayList<>();

    @Column
    @Builder.Default
    private List<String> completedTaskResultsIds = new ArrayList<>();

}
