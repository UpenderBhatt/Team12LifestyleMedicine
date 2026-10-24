package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerController;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerService;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@SessionAttributes("user")
public class LoginRegisterController {

    private  UserService userService;
    private final AnswerService answerService;
    private final taskGenService taskGenService;

    public LoginRegisterController(UserService userService, AnswerService answerService, taskGenService taskGenService) {
        this.userService = userService;
        this.answerService = answerService;
        this.taskGenService = taskGenService;
    }

    @ModelAttribute("user")
    public RegistrationDto registrationDto() {
        return new RegistrationDto();
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm(@ModelAttribute("user") RegistrationDto user) {
        ModelAndView mv = new ModelAndView("auth/register");
        mv.addObject("user",user);
        return mv;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute("user") RegistrationDto user
            ,BindingResult result, Model model){

        ModelAndView mv = new ModelAndView("auth/register");

        ModelAndView mv2 = new ModelAndView("redirect:/login?success");

        ModelAndView mv3 = new ModelAndView("redirect:/register?error");

        UserEntity existingUsersEmail = userService.findByEmail(user.getEmail());
        if (existingUsersEmail != null && existingUsersEmail.getEmail() != null && !existingUsersEmail.getEmail().isEmpty()) {
            return mv3;
        }
        UserEntity existingUsersUsername = userService.findByUsername(user.getUsername());
        if  (existingUsersUsername != null && existingUsersUsername.getUsername() != null && !existingUsersUsername.getUsername().isEmpty()) {
            return mv3;
        }
        if  (result.hasErrors()){
            mv.addObject(
                    "user",user);
            return mv;
        }

        model.addAttribute("user", user);
        return new ModelAndView("redirect:/register/questionnaire");
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(@ModelAttribute("user") RegistrationDto user) {
        ModelAndView mv = new ModelAndView("auth/login");
        mv.addObject("user", user);
        return mv;
    }

    @GetMapping("/register/questionnaire")
    public ModelAndView showQuestionnaire(@ModelAttribute("user") RegistrationDto user) {
        if(user.getEmail() == null || user.getUsername() == null || user.getPassword() == null){
            return new ModelAndView("redirect:/register");
        }

        return new ModelAndView("Questionnaire");
    }

    @PostMapping(value = "/register/questionnaire", consumes = "application/json")
    @ResponseBody
    public String registerQuestionnaire(@RequestBody List<AnswerController.AnswerRequest> answers, @ModelAttribute("user") RegistrationDto user, SessionStatus sessionStatus) {

        UserEntity savedUser = userService.registerUser(user);

        List<Answer> answerEntities = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (AnswerController.AnswerRequest answer : answers) {
            Answer answerEntity = new Answer();
            answerEntity.setQuestionId(answer.getQuestionId());
            answerEntity.setResponse(answer.getResponse());
            answerEntity.setSubmissionDate(now);
            answerEntity.setUserId(savedUser.getId());
            answerEntities.add(answerEntity);
        }

        answerService.saveBatch(answerEntities);

        taskGenService.setTasks(savedUser.getId());

        sessionStatus.setComplete();
        return "OK";
    }

}