package uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration;

import com.google.genai.Client;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class TaskGenServiceTests {

    @SpyBean
    private taskGenService taskGenService;

    @Autowired
    private taskRepository taskRepository;

    @MockBean
    private AnswerRepository answerRepository;

    @MockBean
    private Client aiClient;

    @Test
    @Transactional
    void setTasks_savesGeneratedTasksForUserWithSequentialDates() {
        Long userId = 2L;

        Answer gender = new Answer();
        gender.setUserId(userId);
        gender.setQuestionId(1L);
        gender.setResponse("Male");
        gender.setSubmissionDate(LocalDateTime.now());

        Answer age = new Answer();
        age.setUserId(userId);
        age.setQuestionId(2L);
        age.setResponse("19");
        age.setSubmissionDate(LocalDateTime.now());

        Answer bmi = new Answer();
        bmi.setUserId(userId);
        bmi.setQuestionId(3L);
        bmi.setResponse("25.0");
        bmi.setSubmissionDate(LocalDateTime.now());

        Answer health = new Answer();
        health.setUserId(userId);
        health.setQuestionId(4L);
        health.setResponse("Asthma");
        health.setSubmissionDate(LocalDateTime.now());

        Answer experience = new Answer();
        experience.setUserId(userId);
        experience.setQuestionId(8L);
        experience.setResponse("Beginner");
        experience.setSubmissionDate(LocalDateTime.now());

        Answer goal = new Answer();
        goal.setUserId(userId);
        goal.setQuestionId(11L);
        goal.setResponse("Lose weight");
        goal.setSubmissionDate(LocalDateTime.now());

        Answer motivation = new Answer();
        motivation.setUserId(userId);
        motivation.setQuestionId(12L);
        motivation.setResponse("To feel healthier");
        motivation.setSubmissionDate(LocalDateTime.now());

        Answer confidence = new Answer();
        confidence.setUserId(userId);
        confidence.setQuestionId(13L);
        confidence.setResponse("7");
        confidence.setSubmissionDate(LocalDateTime.now());

        List<Answer> answers = List.of(
                gender, age, bmi, health, experience, goal, motivation, confidence
        );

        doReturn(answers)
                .when(answerRepository)
                .findByUserIdOrderByQuestionIdAsc(anyLong());

        generatedTask Task1 = new generatedTask();
        Task1.setTitle("Day 1 walk");
        Task1.setDescription("30-minute brisk walk");

        generatedTask Task2 = new generatedTask();
        Task2.setTitle("Day 2 walk");
        Task2.setDescription("35-minute brisk walk");

        List<generatedTask> testGeneratedTasks = List.of(Task1,Task2);

        doReturn(testGeneratedTasks)
                .when(taskGenService)
                .generateTasks(org.mockito.ArgumentMatchers.anyString());

        LocalDate today = LocalDate.now();

        taskGenService.setTasks(userId);

        List<task> savedTasks = taskRepository.findByUserIdOrderByTaskDateAsc(userId);

        assertThat(savedTasks).hasSize(2);

        task firstTask = savedTasks.get(0);
        task secondTask = savedTasks.get(1);

        assertThat(firstTask.getTaskTitle()).isEqualTo("Day 1 walk");
        assertThat(firstTask.getTaskDescription()).isEqualTo("30-minute brisk walk");
        assertThat(firstTask.getTaskDate()).isEqualTo(today);
        assertThat(firstTask.getStatus()).isEqualTo(taskStatus.Pending);

        assertThat(secondTask.getTaskTitle()).isEqualTo("Day 2 walk");
        assertThat(secondTask.getTaskDescription()).isEqualTo("35-minute brisk walk");
        assertThat(secondTask.getTaskDate()).isEqualTo(today.plusDays(1));
        assertThat(secondTask.getStatus()).isEqualTo(taskStatus.Pending);
    }
}
