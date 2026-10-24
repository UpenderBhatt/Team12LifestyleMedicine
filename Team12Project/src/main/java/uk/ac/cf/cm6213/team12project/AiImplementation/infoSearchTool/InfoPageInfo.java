package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "InfoPageInfo")
public class InfoPageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String htmlContent;

    public InfoPageInfo() {
    }
}
