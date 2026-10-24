package uk.ac.cf.cm6213.team12project.emailReminder;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;

import java.time.LocalDate;

@Controller
public class reminderController {
    private final taskGenService taskGenService;
    private final taskReminderService taskReminderService;

    public reminderController(taskGenService taskGenService, taskReminderService taskReminderService) {
        this.taskGenService = taskGenService;
        this.taskReminderService = taskReminderService;
    }

    @GetMapping("/admin/reminders")
    public String adminReminders(Model model) {
        model.addAttribute("currentDate", taskGenService.getCurrentSimulatedDate());
        return "admin/reminders";
    }

    @PostMapping("/admin/reminders/next-day")
    public String nextDay() {
        taskGenService.nextDay();
        return "redirect:/admin/reminders";
    }

    @PostMapping("/admin/reminders/send-reminders")
    public String sendReminders() {
        LocalDate currentDate = taskGenService.getCurrentSimulatedDate();
        taskReminderService.sendRemindersForDate(currentDate);
        return "redirect:/admin/reminders";
    }
}
