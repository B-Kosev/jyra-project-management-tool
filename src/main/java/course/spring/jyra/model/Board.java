package course.spring.jyra.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @Column
    private Long id;

    @Column
    private Long projectId;

    @Column
    private Long sprintId;

    @Column
    @Builder.Default
    private List<Long> toDoIds = new ArrayList<>();

    @Column
    @Builder.Default
    private List<Long> inProgressIds = new ArrayList<>();

    @Column
    @Builder.Default
    private List<Long> inReviewIds = new ArrayList<>();

    @Column
    @Builder.Default
    private List<Long> doneIds = new ArrayList<>();

    @Column
    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created = LocalDateTime.now();

    @Column
    @Builder.Default
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modified = LocalDateTime.now();
}
