package course.spring.jyra.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Setter
@ToString
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @NotNull
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
    @OneToOne (mappedBy = "sprint")
    private Project project;

    @NonNull
    @NotNull
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToOne(mappedBy = "sprint")
    private Board board;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id", referencedColumnName = "id")
    private SprintResult result;

    @OneToMany(mappedBy = "sprint")
    private HashSet<Task> tasks = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Sprint sprint = (Sprint) o;
        return id != null && Objects.equals(id, sprint.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
