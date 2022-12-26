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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table
public class Project {
    @Id
    @Column
    private Long id;

    @NonNull
    @NotNull
    @Column
    @Size(min = 2, max = 120, message = "Project title must be between 2 and 120 characters String.")
    private String title;

    @NonNull
    @NotNull
    @Column
    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate = LocalDateTime.now();

    @Size(min = 10, max = 2500, message = "Description must be between 10 and 2500 characters String.")
    @Column
    private String description;

    @NonNull
    @NotNull
    @Column
    private Long ownerId;

    @NonNull
    @NotNull
    @Column
    private List<Long> developersIds = new ArrayList<>();

    @Column
    private Long currentSprintId;

    @Builder.Default
    @Column
    private List<Long> previousSprintResultsIds = new ArrayList<>();

    @Builder.Default
    @Column
    private List<Long> tasksBacklogIds = new ArrayList<>();

    @Column
    private String tags;

    @Column
    private Long projectResultId;

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modified = LocalDateTime.now();
}
