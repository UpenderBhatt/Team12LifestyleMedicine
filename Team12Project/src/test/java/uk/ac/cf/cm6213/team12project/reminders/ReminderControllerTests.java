package uk.ac.cf.cm6213.team12project.reminders;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;
import uk.ac.cf.cm6213.team12project.emailReminder.reminderController;
import uk.ac.cf.cm6213.team12project.emailReminder.taskReminderService;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(reminderController.class)
public class ReminderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private taskGenService taskGenService;

    @MockBean
    private taskReminderService taskReminderService;

    @Test
    @WithMockUser(username = "admin", roles ={"ADMIN"})
    void LoadRemindersPageWithSimulatedDate() throws Exception {
        LocalDate simulatedDate = LocalDate.now();

        Mockito.when(taskGenService.getCurrentSimulatedDate()).thenReturn(simulatedDate);

        mockMvc.perform(get("/admin/reminders"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reminders"))
                .andExpect(model().attribute("currentDate", simulatedDate));
    }

    @Test
    @WithMockUser(username = "admin", roles ={"ADMIN"})
    void SendReminderForSimulatedDate() throws Exception {
        LocalDate simulatedDate = LocalDate.now();
        Mockito.when(taskGenService.getCurrentSimulatedDate()).thenReturn(simulatedDate);

        mockMvc.perform(post("/admin/reminders/send-reminders").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reminders"));

        Mockito.verify(taskReminderService)
                .sendRemindersForDate(simulatedDate);
    }

    @Test
    @WithMockUser(username = "admin", roles ={"ADMIN"})
    void changeSimulatedDate() throws Exception {
        mockMvc.perform(post("/admin/reminders/next-day").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reminders"));

        Mockito.verify(taskGenService).nextDay();
    }
}
