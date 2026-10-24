package uk.ac.cf.cm6213.team12project.riskprofile;

import org.springframework.stereotype.Service;

import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;

import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskProfileServiceImpl implements RiskProfileService {

    private final AnswerRepository answerRepository;
    private final taskGenService taskService;
    private final UserService userService;

    public RiskProfileServiceImpl(AnswerRepository answerRepository,
                                  taskGenService taskService,
                                  UserService userService) {
        this.answerRepository = answerRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Override
    public RiskProfile getProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity user = userService.findByUsername(username);

        return buildProfileForUser(user.getId());
    }

    @Override
    public RiskProfile getProfileForUser(Long userId) {
      return buildProfileForUser(userId);
    }

    private RiskProfile buildProfileForUser(Long userId) {

        List<Answer> answers = answerRepository.findByUserIdOrderByQuestionIdAsc(userId);

        String gender      = getLatestResponseForQuestion(answers, 1L);
        String age    = getLatestResponseForQuestion(answers, 2L);
        String bmi     = getLatestResponseForQuestion(answers, 3L);
        String healthConditions       = getLatestResponseForQuestion(answers, 4L);
        String fruitVegServings            = getLatestResponseForQuestion(answers, 5L);
        String sleepHours        = getLatestResponseForQuestion(answers, 6L);
        String stressFrequency       = getLatestResponseForQuestion(answers, 7L);
        String exerciseFrequency   = getLatestResponseForQuestion(answers, 8L);
        String smokingStatus         = getLatestResponseForQuestion(answers, 9L);
        String alcoholUse   = getLatestResponseForQuestion(answers, 10L);
        String goal         = getLatestResponseForQuestion(answers, 11L);
        String goalReason = getLatestResponseForQuestion(answers, 12L);
        String lifestyleConfidence   = getLatestResponseForQuestion(answers, 13L);
        String readinessToChange     = getLatestResponseForQuestion(answers, 14L);


        LocalDate today = taskService.getCurrentSimulatedDate();

        List<task> tasks = taskService.getTasksForUser(userId);

        String todaysTask = tasks.stream()
                .filter(t -> t.getTaskDate().equals(today))
                .map(task::getTaskTitle)
                .findFirst()
                .orElse("No task scheduled for today.");


        return new RiskProfile(
                gender,
                age,
                bmi,
                healthConditions,
                fruitVegServings,
                sleepHours,
                stressFrequency,
                exerciseFrequency,
                smokingStatus,
                alcoholUse,
                goal,
                goalReason,
                lifestyleConfidence,
                readinessToChange,
                todaysTask
        );
    }

    private String getLatestResponseForQuestion(List<Answer> answers, Long questionId) {
        String latestResponse = "No response recorded";
        LocalDateTime latestDate = null;

        for (Answer answer : answers) {
            if (answer.getQuestionId() != null && answer.getQuestionId().equals(questionId)) {
                if (latestDate == null || answer.getSubmissionDate().isAfter(latestDate)){
                    latestDate = answer.getSubmissionDate();
                    latestResponse = answer.getResponse();
                }
            }
        }
    return latestResponse;
    }
}

