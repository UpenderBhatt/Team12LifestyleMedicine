package uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="tasks", indexes = {
        @Index(name = "index_tasks_user_id", columnList = "userId"),
        @Index(name = "index_tasks_date_status", columnList = "taskDate, status")
})
public class task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private Long userId;
    private String taskTitle;
    private String taskDescription;
    private LocalDate taskDate;

    @Enumerated(EnumType.STRING)
    private taskStatus status = taskStatus.Pending;

    private Boolean locked = false;

    public task() {}

    public void markAsCompleted() {
        if(!locked)
        {
            this.status = taskStatus.Completed;
            this.locked = true;
        }
    }

    public void markAsMissed() {
        if(!locked)
        {
            this.status = taskStatus.Missed;
            this.locked = true;
        }
    }

    public boolean isCompleted()
    {
        return status == taskStatus.Completed;
    }

    public boolean isMissed()
    {
        return status == taskStatus.Missed;
    }

}
