package course.spring.jyra.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
id (generated automatically) - String number;
project - the Project this result is reported for;
endDate - end date for the project;
duration - integer, number of working days for the project;
resultsDescription (optional) - string 10 - 2500 characters String, supporting Markdown syntax;
sprintResults - list of SprintResult for the Sprints completed;
created (generated automatically) - time stamp of the moment the entity was created;
modified (generated automatically) - time stamp of the moment the entity was last modified;
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProjectResult {
    @Column
    @Id
    private Long id;

    @NonNull
    @NotNull
    @Column
    private Long projectId;

    @NonNull
    @NotNull
    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime endDate=LocalDateTime.now();

    @NonNull
    @NotNull
    @Column
    private long duration;

    @Size(min = 10, max = 2500, message = "String must be between 10 and 2500 characters String, supporting Markdown syntax.")
    @Column
    private String resultsDescription;

    @NonNull
    @NotNull
    @Column
        private List<Long> sprintResultListIds = new ArrayList<>();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime modified = LocalDateTime.now();

}
