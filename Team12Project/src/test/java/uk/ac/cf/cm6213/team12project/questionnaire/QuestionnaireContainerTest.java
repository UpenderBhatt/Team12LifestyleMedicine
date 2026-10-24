package uk.ac.cf.cm6213.team12project.questionnaire;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class QuestionnaireContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveBatchAnswersThroughWholeStack() throws Exception {
        List<AnswerController.AnswerRequest> answers = List.of(
                createAnswer(1L, "Answer one"),
                createAnswer(2L, "Answer two")
        );

        mockMvc.perform(
                        post("/batch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(answers))
                )
                .andExpect(status().isOk());
    }

    private AnswerController.AnswerRequest createAnswer(Long questionId, String response) {
        AnswerController.AnswerRequest a = new AnswerController.AnswerRequest();
        a.setQuestionId(questionId);
        a.setResponse(response);
        return a;
    }
}
