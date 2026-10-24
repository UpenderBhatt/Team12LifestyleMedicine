package uk.ac.cf.cm6213.team12project.riskprofile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RiskProfileServiceImplTest {

    private AnswerRepository answerRepository;
    private taskGenService taskService;
    private UserService userService;

    private RiskProfileServiceImpl service;

    @BeforeEach
    void setup() {
        answerRepository = mock(AnswerRepository.class);
        taskService = mock(taskGenService.class);
        userService = mock(UserService.class);

        service = new RiskProfileServiceImpl(answerRepository, taskService, userService);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testProfileBuildsCorrectly() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        when(userService.findByUsername(anyString())).thenReturn(user);

        Answer gender = new Answer();
        gender.setQuestionId(1L);
        gender.setResponse("Male");
        gender.setSubmissionDate(LocalDateTime.now());

        when(answerRepository.findByUserIdOrderByQuestionIdAsc(1L))
                .thenReturn(List.of(gender));

        LocalDate simulatedDate = LocalDate.of(2025, 1, 1);

        task todayTask = new task();
        todayTask.setTaskTitle("Drink Water");
        todayTask.setTaskDate(simulatedDate);

        when(taskService.getCurrentSimulatedDate()).thenReturn(simulatedDate);
        when(taskService.getTasksForUser(1L)).thenReturn(List.of(todayTask));

        RiskProfile profile = service.getProfile();

        assertEquals("Male", profile.getGender());
        assertEquals("Drink Water", profile.getTodaysTask());
    }

}
