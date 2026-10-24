package uk.ac.cf.cm6213.team12project.questionnaire;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/questionnaire")
    public String questionnairePage() {

        return "Questionnaire";
    }
}

