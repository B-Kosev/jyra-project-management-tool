package course.spring.jyra.model;

/*
id (generated automatically) - long number;
sprint - the Sprint this result is reported for;
teamVelocity - integer number in effort units (sum of effort units for all tasks completed during the sprint);
resultsDescription (optional) - string 10 - 2500 characters long, supporting Markdown syntax;
tasksResults - list of TaskResult for the Tasks completed in the Sprint;
created (generated automatically) - time stamp of the moment the entity was created;
modified (generated automatically) - time stamp of the moment the entity was last modified;
 */

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "sprintResults")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class SprintResult {
    @Id
    private long id;

    @NotNull
    @NonNull
    private Sprint sprint;

    @NonNull
    @NotNull
    private int teamVelocity;

    @Size(min = 10, max = 2500, message = "String must be between 10 and 2500 characters long, supporting Markdown syntax.")
    private String resultsDescription;

    @NonNull
    @NotNull
    private List<TaskResult> taskResults;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modified = LocalDateTime.now();

    private int calcTeamVelocity(){
        int result=0;
        for(int i=0;i<taskResults.size();i++){
            result+=taskResults.get(i).getActualEffort();
        }
        return result;
    }
}