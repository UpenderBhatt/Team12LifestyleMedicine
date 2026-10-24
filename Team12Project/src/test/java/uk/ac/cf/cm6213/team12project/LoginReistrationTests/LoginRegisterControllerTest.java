package uk.ac.cf.cm6213.team12project.LoginReistrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.LoginRegisterController;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.RegistrationDto;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerController;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginRegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AnswerService answerService;

    @MockBean
    private taskGenService taskGenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRedirectToErrorIfUserExists() throws Exception {
        RegistrationDto dto = new RegistrationDto();
        dto.setEmail("test@test.com");
        dto.setUsername("existingUser");
        dto.setPassword("password");

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test@test.com");

        given(userService.findByEmail("test@test.com")).willReturn(existingUser);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .flashAttr("user", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error"));
    }

    @Test
    void shouldProcessQuestionnaireJsonSubmission() throws Exception {
        RegistrationDto sessionUser = new RegistrationDto();
        sessionUser.setUsername("test");
        sessionUser.setEmail("test@test.com");
        sessionUser.setPassword("test");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);

        given(userService.registerUser(any(RegistrationDto.class))).willReturn(savedUser);

        List<AnswerController.AnswerRequest> answers = new ArrayList<>();

        mockMvc.perform(post("/register/questionnaire")
                        .with(csrf())
                        .sessionAttr("user", sessionUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answers)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}