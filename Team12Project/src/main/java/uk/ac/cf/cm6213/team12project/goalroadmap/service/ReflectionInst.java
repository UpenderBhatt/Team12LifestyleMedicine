package uk.ac.cf.cm6213.team12project.goalroadmap.service;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reflections")
public class ReflectionInst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reflectionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long taskId;

    private LocalDate dateWritten;

    @Column(columnDefinition = "TEXT")
    private String content;

    public ReflectionInst() {}

    public ReflectionInst(Long userId, Long taskId, String content) {
        this.userId = userId;
        this.taskId = taskId;
        this.content = content;
    }

    @PrePersist
    public void onCreate() {
        this.dateWritten = LocalDate.now();
    }

    public Long getId() { return reflectionId; }
    public Long getUserId() { return userId; }
    public Long getTaskId() { return taskId; }
    public LocalDate getDateWritten() { return dateWritten; }
    public String getContent() { return content; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public void setContent(String content) { this.content = content; }
    public void setDateWritten(LocalDate dateWritten) { this.dateWritten = dateWritten; }
}
