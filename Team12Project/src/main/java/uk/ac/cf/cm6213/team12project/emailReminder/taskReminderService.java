package uk.ac.cf.cm6213.team12project.emailReminder;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskRepository;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskStatus;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
public class taskReminderService {

    private final taskRepository taskRepository;
    private final UserService userService;
    private final emailService emailService;

    public taskReminderService(taskRepository taskRepository, UserService userService, emailService emailService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.emailService = emailService;
    }


    public void sendRemindersForDate(LocalDate currentDate) {
        List<task> pendingTasks = taskRepository.findByTaskDateAndStatus(currentDate, taskStatus.Pending);

        for (task task : pendingTasks) {
            UserEntity user = userService.findByUsername((userService.findById(task.getUserId())).getUsername());

            if(user==null || user.getEmail() == null){
                continue;
            }

            sendReminderEmail(user, task, currentDate);
        }
    }

    private void sendReminderEmail(UserEntity user, task task, LocalDate currentDate) {

        String subject = "Today's task is pending!";

        String body = """
                        Hello %s,
    
                        You have a scheduled task for %s which is: 
                        
                        %s
                        %s
                        
                        Please make sure to complete it by the end of the day and mark as complete!
                        
                        Best Of Luck!
                      """.formatted(user.getUsername(), currentDate, task.getTaskTitle(), task.getTaskDescription());

        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
