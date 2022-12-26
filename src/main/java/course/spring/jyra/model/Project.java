package course.spring.jyra.model;

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

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table
public class Project {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

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
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User owner;

	@OneToOne(mappedBy = "project")
	private Board board;

	@OneToOne
	@JoinColumn(name = "sprint_id", referencedColumnName = "id")
	private Sprint sprint;

	@OneToOne(mappedBy = "project")
	private ProjectResult projectResult;

	@OneToMany(mappedBy = "project")
	private Set<Task> tasks = new HashSet<>();

	@Column
	private String tags;

	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column
	private LocalDateTime created = LocalDateTime.now();

	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime modified = LocalDateTime.now();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Project project = (Project) o;
		return id != null && Objects.equals(id, project.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
