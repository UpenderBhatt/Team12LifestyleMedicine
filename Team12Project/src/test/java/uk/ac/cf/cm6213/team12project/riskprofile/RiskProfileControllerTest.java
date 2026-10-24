package uk.ac.cf.cm6213.team12project.riskprofile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserRepository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RiskProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiskProfileService riskProfileService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "testUser", roles = {"PATIENT"})
    void testProfilePageLoadsAndReturnsModelData() throws Exception {

        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        RiskProfile mockProfile = new RiskProfile(
                "Male", "20", "22", "None", "3", "8",
                "Low", "4", "Non-smoker", "Low",
                "Fitness", "Health", "Medium", "High",
                "Run"
        );
        when(riskProfileService.getProfile()).thenReturn(mockProfile);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(view().name("riskprofile/profile"));
    }
}
