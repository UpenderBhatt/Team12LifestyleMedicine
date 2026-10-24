package uk.ac.cf.cm6213.team12project.questionnaire;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
class QuestionnairePageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldRenderQuestionnairePage() throws Exception {
        mockMvc.perform(get("/questionnaire"))
                .andExpect(status().isOk())
                .andExpect(view().name("Questionnaire"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Health Questionnaire")));
    }

    @Test
    @WithMockUser
    void shouldHaveAllQuestionInputsAndForm() throws Exception {
        mockMvc.perform(get("/questionnaire"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<form id=\"questionnaireForm\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"1\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"14\"")));
    }

}


