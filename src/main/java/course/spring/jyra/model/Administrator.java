package course.spring.jyra.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
@Data
@AllArgsConstructor
@SuperBuilder
public class Administrator extends User {
}
