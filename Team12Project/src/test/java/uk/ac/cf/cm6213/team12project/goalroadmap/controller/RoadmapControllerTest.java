package uk.ac.cf.cm6213.team12project.goalroadmap.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionInst;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionService;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoadmapController.class)
public class RoadmapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReflectionService reflectionService;

    @MockBean
    private UserService userService;

    @MockBean
    private taskGenService taskService;

    @Test
    @WithMockUser(username = "testUser")
    void goalRoadmapViewAndModel() throws Exception {
        UserEntity user = Mockito.mock(UserEntity.class);
        when(user.getId()).thenReturn(42L);

        when(userService.findByUsername("testUser")).thenReturn(user);
        when(taskService.getTasksForUser(42L)).thenReturn(Collections.emptyList());
        when(taskService.getCurrentSimulatedDate()).thenReturn(LocalDate.of(2024, 1, 1));
        when(taskService.getDateOffset()).thenReturn(0);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("goal-roadmap"))
                .andExpect(model().attributeExists("weeks"))
                .andExpect(model().attribute("userId", 42L))
                .andExpect(model().attribute("todayDate", LocalDate.of(2024, 1, 1)))
                .andExpect(model().attribute("dateOffset", 0));
    }

    @Test
    @WithMockUser
    void getReflectionWhenPresent() throws Exception {
        ReflectionInst reflection = new ReflectionInst(1L, 2L, "Reflection content");
        when(reflectionService.getReflectionByTask(2L)).thenReturn(Optional.of(reflection));

        mockMvc.perform(get("/api/reflections/task/{taskId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.content").value("Reflection content"));
    }

    @Test
    @WithMockUser
    void getReflectionWhenNotPresent() throws Exception {
        when(reflectionService.getReflectionByTask(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reflections/task/{taskId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false))
                .andExpect(jsonPath("$.content").doesNotExist());
    }

    @Test
    @WithMockUser
    void saveReflectionReturnsSuccessful() throws Exception {
        ReflectionInst reflection = new ReflectionInst(1L, 2L, "Content");
        reflection.setDateWritten(LocalDate.now());

        java.lang.reflect.Field field = ReflectionInst.class.getDeclaredField("reflectionId");
        field.setAccessible(true);
        field.set(reflection, 100L);

        when(reflectionService.saveOrUpdateReflection(eq(1L), eq(2L), eq("Content")))
                .thenReturn(reflection);

        String body = """
                {
                  "userId": 1,
                  "taskId": 2,
                  "content": "Content"
                }
                """;

        mockMvc.perform(
                        post("/api/reflections/save")
                                .with(csrf())
                                .contentType("application/json")
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.reflectionId").value(100L));
    }

    @Test
    @WithMockUser
    void updateTaskStatusReturnsSuccess() throws Exception {
        String body = """
                {
                  "status": "Completed"
                }
                """;

        mockMvc.perform(
                        post("/api/tasks/{taskId}/status", 5L)
                                .with(csrf())
                                .contentType("application/json")
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(taskService).updateTaskStatus(eq(5L), any());
    }

    @Test
    @WithMockUser
    void nextDayMessageIsReturned() throws Exception {
        when(taskService.getCurrentSimulatedDate())
                .thenReturn(LocalDate.of(2024, 1, 2));

        mockMvc.perform(get("/test/next-day"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Moved forward one day")))
                .andExpect(content().string(containsString("2024-01-02")));

        verify(taskService).nextDay();
    }

    @Test
    @WithMockUser
    void previousDayMessageIsReturned() throws Exception {
        when(taskService.getCurrentSimulatedDate())
                .thenReturn(LocalDate.of(2024, 1, 1));

        mockMvc.perform(get("/test/previous-day"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Moved backward one day")))
                .andExpect(content().string(containsString("2024-01-01")));

        verify(taskService).previousDay();
    }
}
