package uk.ac.cf.cm6213.team12project.questionnaire;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "answers", indexes = {
        @Index(name = "index_answers_user_id", columnList = "userId")
})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String response;

    private LocalDateTime submissionDate;

    @Column(name = "question_id")
    private Long questionId;

    public Answer() {}

    public Answer(String response, LocalDateTime submissionDate, Long questionId) {
        this.response = response;
        this.submissionDate = submissionDate;
        this.questionId = questionId;
    }
    public Long getId() {return id; }

    public Long getUserId() {return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getResponse() {return response; }
    public void setResponse(String response) {this.response = response; }

    public LocalDateTime getSubmissionDate() {return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) {this.submissionDate = submissionDate; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
}