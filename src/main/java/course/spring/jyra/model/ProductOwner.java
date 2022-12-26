package course.spring.jyra.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductOwner extends User {
    @Column
    @Builder.Default
    private List<String> projectsIds = new ArrayList<>();

    @Column
    @Builder.Default
    private List<String> completedProjectResultsIds = new ArrayList<>();
}
