package uk.ac.cf.cm6213.team12project.LoginReistrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.RegistrationDto;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginRegisterContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private RegistrationDto createValidRegistrationDto() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("TestyTesty");
        dto.setEmail("Testy@Testy.test");
        dto.setPassword("Test");
        return dto;
    }

    @Test
    void shouldRegisterUserAndRedirectToQuestionnaire() throws Exception {

        RegistrationDto dto = createValidRegistrationDto();

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .flashAttr("user", dto))
                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/register/questionnaire"));


        UserEntity savedUser = userService.findByEmail(dto.getEmail());

    }

    @Test
    void shouldFailRegistrationIfEmailIsAlreadyUsed() throws Exception {

        RegistrationDto initialDto = createValidRegistrationDto();
        userService.registerUser(initialDto);


        RegistrationDto duplicateDto = new RegistrationDto();
        duplicateDto.setUsername("Test");
        duplicateDto.setEmail(initialDto.getEmail());
        duplicateDto.setPassword("bob");

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .flashAttr("user", duplicateDto))
                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/register?error"));
    }
}