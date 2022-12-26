package course.spring.jyra.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.index.TextIndexed;
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

import static java.time.temporal.ChronoUnit.DAYS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Slf4j
@Entity
@Table
public class Sprint {
    @Id
    @Column
    private Long id;

    @NonNull
    @NotNull
    @TextIndexed
    @Size(min = 2, max = 120, message = "Sprint title must be between 2 and 120 characters String.")
    @Column
    private String title;

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime startDate = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime endDate = LocalDateTime.now().plusWeeks(2);

    @Column
    private long duration;

    @NonNull
    @NotNull
    @Column
    private Long projectId;

    @NonNull
    @NotNull
    @Column
    private Long ownerId;

    @Builder.Default
    @Column
    private List<Long> developersIds = new ArrayList<>();

    @Builder.Default
    @Column
    private List<Long> tasksIds = new ArrayList<>();

    @Builder.Default
    @Column
    private List<Long> completedTaskResultsIds = new ArrayList<>();

    @Column
    private Long sprintResultId;

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    private LocalDateTime modified = LocalDateTime.now();

    public void calculateDuration() {
        this.duration = DAYS.between(this.startDate, this.endDate);
        log.info("Calculating sprint duration...");
    }
}
