package uk.ac.cf.cm6213.team12project.goalroadmap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FullTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private taskGenService taskService;

    @Test
    @WithMockUser(username = "testUser")
    void homepageLoads() throws Exception {

        UserEntity user = Mockito.mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        when(userService.findByUsername("testUser")).thenReturn(user);
        when(taskService.getTasksForUser(1L)).thenReturn(Collections.emptyList());
        when(taskService.getCurrentSimulatedDate()).thenReturn(LocalDate.now());
        when(taskService.getDateOffset()).thenReturn(0);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("goal-roadmap"));
    }
}
