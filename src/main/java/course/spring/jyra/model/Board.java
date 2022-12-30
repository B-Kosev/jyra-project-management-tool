package course.spring.jyra.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(mappedBy = "board")
	@JsonIgnore
	private Project project;

	@OneToOne(mappedBy = "board")
	@JsonIgnore
	private Sprint sprint;

	@Column
	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime created = LocalDateTime.now();

	@Column
	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime modified = LocalDateTime.now();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Board board = (Board) o;
		return id != null && Objects.equals(id, board.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
