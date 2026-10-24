package uk.ac.cf.cm6213.team12project.riskprofile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.when;

@SpringBootTest
public class RiskProfileServiceTest {

    @Autowired
    private RiskProfileService riskProfileService;

    @MockBean
    private AnswerRepository answerRepository;

    @MockBean
    private taskGenService taskService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRiskProfileIsBuiltFromMockData() {
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

        // act
        RiskProfile profile = riskProfileService.getProfile();

        // assert
        assertEquals("Male", profile.getGender());
        assertEquals("Drink Water", profile.getTodaysTask());
    }
}
