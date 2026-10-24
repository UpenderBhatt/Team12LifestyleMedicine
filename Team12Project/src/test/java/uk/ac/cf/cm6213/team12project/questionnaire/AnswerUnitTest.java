package uk.ac.cf.cm6213.team12project.questionnaire;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AnswerUnitTest {

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Answer answer = new Answer("Test response", now, 5L);
        answer.setUserId(42L);
        assertEquals("Test response", answer.getResponse());
        assertEquals(now, answer.getSubmissionDate());
        assertEquals(5L, answer.getQuestionId());
        assertEquals(42L, answer.getUserId());
    }
}
