package uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface taskRepository extends JpaRepository<task, Long> {
    List<task> findByUserIdOrderByTaskDateAsc(Long userId);
    Optional<task> findByUserIdAndTaskDate(Long userId, LocalDate taskDate);
    List<task> findByTaskDateAndStatus(LocalDate taskDate, taskStatus taskStatus);
}
