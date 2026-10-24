package uk.ac.cf.cm6213.team12project.questionnaire;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/submitQuestionnaire")
    public Answer saveAnswer(@RequestParam Long questionId,
                             @RequestParam String response) {
        Answer answer = new Answer(response, LocalDateTime.now(), questionId);
        return answerService.saveAnswer(answer);
    }

    @PostMapping("/batch")
    public List<Answer> saveAnswers(@RequestBody List<AnswerRequest> answers) {
        List<Answer> entities = answers.stream()
                .map(req -> new Answer(req.getResponse(), LocalDateTime.now(), req.getQuestionId()))
                .collect(Collectors.toList());
        return answerService.saveBatch(entities);
    }

    public static class AnswerRequest {
        private String response;
        private Long questionId;

        public Long getQuestionId() {return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getResponse() {return response; }
        public void setResponse(String response) { this.response = response; }
    }
}