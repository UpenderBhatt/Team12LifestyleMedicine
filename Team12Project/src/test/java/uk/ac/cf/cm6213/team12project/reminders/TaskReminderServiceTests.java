package uk.ac.cf.cm6213.team12project.reminders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskRepository;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskStatus;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.emailReminder.emailService;
import uk.ac.cf.cm6213.team12project.emailReminder.taskReminderService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
public class TaskReminderServiceTests {
    @Mock
    private taskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private emailService emailService;

    @InjectMocks
    private taskReminderService taskReminderService;

    @Test
    void SendReminderForPendingTasks(){
        LocalDate simulatedDate = LocalDate.now();

        task task = new task();
        task.setUserId(1L);
        task.setTaskTitle("10 minute run");
        task.setTaskDescription("Go for a short fast paced run");
        task.setTaskDate(simulatedDate);
        task.setStatus(taskStatus.Pending);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@email");

        Mockito.when(taskRepository.findByTaskDateAndStatus(simulatedDate, taskStatus.Pending)).thenReturn(List.of(task));
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.when(userService.findByUsername("user1")).thenReturn(user);

        taskReminderService.sendRemindersForDate(simulatedDate);

        Mockito.verify(emailService, Mockito.times(1))
                .sendEmail(eq("user1@email"), eq("Today's task is pending!"), contains("10 minute run"));

    }

    @Test
    void NoRemindersIfNoPendingTasks(){
        LocalDate simulatedDate = LocalDate.now();

        Mockito.when(taskRepository.findByTaskDateAndStatus(simulatedDate, taskStatus.Pending)).thenReturn(List.of());

        taskReminderService.sendRemindersForDate(simulatedDate);

        Mockito.verifyNoInteractions(emailService);
    }
}
