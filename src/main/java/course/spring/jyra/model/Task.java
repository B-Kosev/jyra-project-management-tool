package course.spring.jyra.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table
public class Task {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NonNull
	@Column
	private TaskType taskType;

	@NotNull
	@NonNull
	@Size(min = 2, max = 120, message = "String must be between 2 and 120 characters String")
	@Column
	private String title;

	@ManyToOne
	@JoinColumn(name = "reporter_id")
	private User addedBy;

	@NotNull
	@NonNull
	@Column
	private int estimatedEffort;

	@NotNull
	@NonNull
	@Builder.Default
	@Column
	private TaskStatus status = TaskStatus.TO_DO;

	@ManyToOne
	@JoinColumn(name = "sprint_id")
	// @ToString.Exclude
	private Sprint sprint;

	@ManyToOne
	@JoinColumn(name = "project_id")
	// @ToString.Exclude
	private Project project;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "task_result_id")
	@JsonIgnore
	private TaskResult taskResult;

	@Column
	@Size(min = 10, max = 2500, message = "String must be between 10 and 2500 characters String, supporting Markdown syntax")
	private String description;

	@NotNull
	@NonNull
	@Column
	private String tags;

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
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Task task = (Task) o;
		return id != null && Objects.equals(id, task.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}