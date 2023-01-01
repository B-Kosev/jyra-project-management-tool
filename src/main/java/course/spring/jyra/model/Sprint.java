package course.spring.jyra.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

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

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "board_id")
	private Board board;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "sprint_result_id")
	private SprintResult sprintResult;

	@Builder.Default
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sprint")
	private Set<Task> tasks = new HashSet<>();

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
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Sprint sprint = (Sprint) o;
		return id != null && Objects.equals(id, sprint.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
