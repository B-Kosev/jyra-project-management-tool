package course.spring.jyra.model;

/*
id (generated automatically) - String number;
sprint - the Sprint this result is reported for;
teamVelocity - integer number in effort units (sum of effort units for all tasks completed during the sprint);
resultsDescription (optional) - string 10 - 2500 characters String, supporting Markdown syntax;
tasksResults - list of TaskResult for the Tasks completed in the Sprint;
created (generated automatically) - time stamp of the moment the entity was created;
modified (generated automatically) - time stamp of the moment the entity was last modified;
 */

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class SprintResult {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @Builder.Default
    @Column
    private int teamVelocity = 0;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SprintResult that = (SprintResult) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
