package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.AiImplementation.DTOs.QuestionnaireDto;

import java.util.List;

public interface InfoSearchService {
    void infoPageGen(String GenMessage);
    String GetPrompt();
}
