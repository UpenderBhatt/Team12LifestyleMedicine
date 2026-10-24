package uk.ac.cf.cm6213.team12project.questionnaire;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AnswerService {
    private AnswerRepository answerRepository;

    public AnswerService(final AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> saveBatch(List<Answer> answers) {
        return answerRepository.saveAll(answers);
    }
}