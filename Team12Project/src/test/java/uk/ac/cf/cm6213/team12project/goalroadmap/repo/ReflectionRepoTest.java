package uk.ac.cf.cm6213.team12project.goalroadmap.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionInst;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReflectionRepoTest {

    @Autowired
    private ReflectionRepo reflectionRepo;

    @Test
    void findByTaskIdReturnsSavedEntity() {
        ReflectionInst reflection = new ReflectionInst(1L, 2L, "Some content");
        ReflectionInst saved = reflectionRepo.save(reflection);

        Optional<ReflectionInst> result = reflectionRepo.findByTaskId(2L);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("Some content", result.get().getContent());
    }

    @Test
    void findByTaskIdReturnsEmptyIfNotFound() {
        Optional<ReflectionInst> result = reflectionRepo.findByTaskId(999L);
        assertTrue(result.isEmpty());
    }
}
