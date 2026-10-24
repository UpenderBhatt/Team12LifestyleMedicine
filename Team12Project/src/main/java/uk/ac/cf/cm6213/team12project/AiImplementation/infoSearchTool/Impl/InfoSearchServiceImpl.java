package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool.Impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.AiImplementation.DTOs.QuestionnaireDto;
import uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool.InfoPageInfo;
import uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool.InfoPageInfoRepository;
import uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool.InfoSearchService;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;


@Service
public class InfoSearchServiceImpl implements InfoSearchService {

    private final InfoPageInfoRepository infoPageInfoRepository;
    private  Client aiClient;
    private static String modelId = "gemini-2.5-flash";
    private final AnswerRepository answerRepository;
    private final UserService userService;

    public InfoSearchServiceImpl(Client aiClient, AnswerRepository answerRepository, UserService userService, InfoPageInfoRepository infoPageInfoRepository) {
        this.aiClient = aiClient;
        this.answerRepository = answerRepository;
        this.userService = userService;
        this.infoPageInfoRepository = infoPageInfoRepository;
    }

    @Override
    public void infoPageGen(String GenMessage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        GenerateContentResponse response = aiClient.models.generateContent(modelId, GenMessage, null);

        String Response = response.text();

        InfoPageInfo infoPageInfo = new InfoPageInfo();

        String username = auth.getName();
        UserEntity user = userService.findByUsername(username);

        infoPageInfo.setUserId(user.getId());
        infoPageInfo.setHtmlContent(Response);

        infoPageInfoRepository.save(infoPageInfo);
    }

    @Override
    public String GetPrompt() {
        BuildPrompt buildPrompt = new BuildPrompt();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByUsername(auth.getName());

        QuestionnaireDto userInfo = getTaskGenInfo(user.getId());

        return buildPrompt.buildPrompt(userInfo);
    }

    private QuestionnaireDto getTaskGenInfo(Long userId) {
        List<Answer> answers = answerRepository.findByUserIdOrderByQuestionIdAsc(userId);

        Map<Long, String> byQuestionId = answers.stream()
                .collect(Collectors.toMap(
                        Answer::getQuestionId,
                        Answer::getResponse,
                        (a, b) -> a
                ));

        QuestionnaireDto taskGenInfo = new QuestionnaireDto();

        taskGenInfo.setGender(byQuestionId.get(1L));
        taskGenInfo.setAge(Integer.parseInt(byQuestionId.get(2L)));
        taskGenInfo.setBmi(parseDouble(byQuestionId.get(3L)));
        taskGenInfo.setHealthCondition(byQuestionId.get(4L));
        taskGenInfo.setExperience(byQuestionId.get(8L));
        taskGenInfo.setTargetGoal(byQuestionId.get(11L));
        taskGenInfo.setMotivation(byQuestionId.get(12L));
        taskGenInfo.setConfidenceLevel(Integer.parseInt(byQuestionId.get(13L)));
        taskGenInfo.setGoalCategory("General fitness");
        taskGenInfo.setStartDate(LocalDate.now());
        taskGenInfo.setDuration(42);

        return taskGenInfo;
    }

}
