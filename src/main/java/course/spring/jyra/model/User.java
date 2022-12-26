package course.spring.jyra.model;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class User implements UserDetails {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NonNull
	@NotNull
	@Column
	@Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters long.")
	private String firstName;

	@NonNull
	@NotNull
	@Column
	@Size(min = 2, max = 15, message = "Last name must be between 2 and 15 characters long.")
	private String lastName;

	@NonNull
	@NotNull
	@Column
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;

	@NonNull
	@NotNull
	@Column
	@Size(min = 2, max = 15, message = "Username must e between 2 and 15 characters long.")
	private String username;

	@NonNull
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}")
	@NotNull
	@Column
	private String password;

	@NonNull
	@NotNull
	@NotEmpty
	@Builder.Default
	@Column
	private Role role = Role.DEVELOPER;

	@Size(min = 10, max = 250, message = "Contacts must be between 10 and 250 characters long.")
	@Column
	private String contacts;

	@Builder.Default
	@Column
	private UserStatus status = UserStatus.CHANGE_PASSWORD;

	@Builder.Default
	@Column
	private String imageUrl = "images/default.jpg";

	@Builder.Default
	@Column
	private boolean active = true;

	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column
	private LocalDateTime created = LocalDateTime.now();

	@Builder.Default
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column
	private LocalDateTime modified = LocalDateTime.now();

	@OneToMany(mappedBy = "owner")
	private Set<Project> projects;

	@OneToMany(mappedBy = "owner")
	private Set<Sprint> sprints;

	@Builder.Default
	@JsonIgnore
	@OneToMany(mappedBy = "addedBy")
	private Set<Task> tasks = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isActive();
	}

	@Override
	public boolean isEnabled() {
		return isActive();
	}

	public String printRoles() {
		return role.getReadable();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
