package uk.ac.cf.cm6213.team12project.goalroadmap.service;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionInstTest {

    @Test
    void dateWrittenIsSet() {
        ReflectionInst reflection = new ReflectionInst(1L, 2L, "Some content");

        assertNull(reflection.getDateWritten());

        reflection.onCreate();

        assertNotNull(reflection.getDateWritten());
        assertEquals(LocalDate.now(), reflection.getDateWritten());
    }

    @Test
    void constructorSetsFields() {
        Long userId = 10L;
        Long taskId = 20L;
        String content = "Hello world";

        ReflectionInst reflection = new ReflectionInst(userId, taskId, content);

        assertEquals(userId, reflection.getUserId());
        assertEquals(taskId, reflection.getTaskId());
        assertEquals(content, reflection.getContent());
    }

    @Test
    void settersAndGettersWork() {
        ReflectionInst reflection = new ReflectionInst();
        reflection.setUserId(5L);
        reflection.setTaskId(6L);
        reflection.setContent("Test content");
        LocalDate date = LocalDate.of(2025, 12, 5);
        reflection.setDateWritten(date);

        assertEquals(5L, reflection.getUserId());
        assertEquals(6L, reflection.getTaskId());
        assertEquals("Test content", reflection.getContent());
        assertEquals(date, reflection.getDateWritten());
    }
}
