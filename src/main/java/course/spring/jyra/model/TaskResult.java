package course.spring.jyra.model;

/*
id (generated automatically) - String number;
task - the Task this result is reported for;
actualEffort - integer number in effort units;
verifiedBy - the ProductOwner" or Developer that has verified the Task successful completion;
resultsDescription (optional) - string 10 - 2500 characters String, supporting Markdown syntax;
created (generated automatically) - time stamp of the moment the entity was created;
modified (generated automatically) - time stamp of the moment the entity was last modified;
 */

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table
public class TaskResult {
    @Id
    @Column
    private Long id;

    @NotNull
    @NonNull
    @Column
    private Long taskId;

    @NotNull
    @NonNull
    @Column
    private int actualEffort;

    @NotNull
    @NonNull
    @Column
    private Long verifiedById;

    @Size(min = 10, max = 2500, message = "String must be between 10 and 2500 characters String, supporting Markdown syntax.")
    @Column
    private String resultsDescription;

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime modified = LocalDateTime.now();

}
