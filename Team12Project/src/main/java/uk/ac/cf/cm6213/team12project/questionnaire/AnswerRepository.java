package uk.ac.cf.cm6213.team12project.questionnaire;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByUserIdOrderByQuestionIdAsc(Long userId);
}
