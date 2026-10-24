package uk.ac.cf.cm6213.team12project.goalroadmap.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.ac.cf.cm6213.team12project.goalroadmap.repo.ReflectionRepo;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReflectionServiceTest {

    private ReflectionRepo reflectionRepo;
    private ReflectionService reflectionService;

    @BeforeEach
    void setUp() {
        reflectionRepo = mock(ReflectionRepo.class);
        reflectionService = new ReflectionService(reflectionRepo);
    }

    @Test
    void saveOrUpdateReflectionIfNotExisting() {
        Long userId = 1L;
        Long taskId = 1L;
        String content = "New reflection";

        when(reflectionRepo.findByTaskId(taskId)).thenReturn(Optional.empty());
        when(reflectionRepo.save(any(ReflectionInst.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReflectionInst result = reflectionService.saveOrUpdateReflection(userId, taskId, content);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(taskId, result.getTaskId());
        assertEquals(content, result.getContent());

        verify(reflectionRepo).findByTaskId(taskId);
        verify(reflectionRepo).save(any(ReflectionInst.class));
    }

    @Test
    void saveOrUpdateReflectionIfExists() {
        Long userId = 1L;
        Long taskId = 2L;
        String oldContent = "Old content";
        String newContent = "Updated content";

        ReflectionInst existing = new ReflectionInst(userId, taskId, oldContent);
        existing.setDateWritten(LocalDate.of(2023, 1, 1));

        when(reflectionRepo.findByTaskId(taskId)).thenReturn(Optional.of(existing));
        when(reflectionRepo.save(any(ReflectionInst.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReflectionInst result = reflectionService.saveOrUpdateReflection(userId, taskId, newContent);

        assertEquals(newContent, result.getContent());
        assertEquals(taskId, result.getTaskId());
        assertEquals(userId, result.getUserId());
        assertEquals(LocalDate.now(), result.getDateWritten());

        verify(reflectionRepo).findByTaskId(taskId);

        ArgumentCaptor<ReflectionInst> captor = ArgumentCaptor.forClass(ReflectionInst.class);
        verify(reflectionRepo).save(captor.capture());

        ReflectionInst saved = captor.getValue();
        assertEquals(newContent, saved.getContent());
    }

    @Test
    void getReflectionByTask() {
        Long taskId = 5L;
        ReflectionInst reflection = new ReflectionInst(1L, taskId, "Content");

        when(reflectionRepo.findByTaskId(taskId)).thenReturn(Optional.of(reflection));

        Optional<ReflectionInst> result = reflectionService.getReflectionByTask(taskId);

        assertTrue(result.isPresent());
        assertEquals(reflection, result.get());
        verify(reflectionRepo).findByTaskId(taskId);
    }
}
