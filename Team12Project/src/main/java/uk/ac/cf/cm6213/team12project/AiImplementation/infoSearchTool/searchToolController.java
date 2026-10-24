package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserRepository;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;

@Controller
public class searchToolController {

    private UserService userService;
    private InfoSearchService infoSearchService;
    private InfoPageInfoRepository infoPageInfoRepository;

    public searchToolController(UserService userService, InfoSearchService infoSearchService, InfoPageInfoRepository infoPageInfoRepository) {
        this.userService = userService;
        this.infoSearchService = infoSearchService;
        this.infoPageInfoRepository = infoPageInfoRepository;
    }

    @GetMapping("/infoSearch")
    public ModelAndView searchTool() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mv2 = new ModelAndView("InfoPage/InfoSearchNotMadeYet");

        String username = auth.getName();
        UserEntity user = userService.findByUsername(username);

        InfoPageInfo infoPageInfo = infoPageInfoRepository.findByUserId(user.getId());

        if (infoPageInfo != null) {
            ModelAndView mv = new ModelAndView("InfoPage/InfoSearch");
            mv.addObject("infoPageInfo", infoPageInfo);
            return mv;
        }

        return mv2;
    }

    @PostMapping("/infoSearchGen")
    public String GenSearchTool() {
        infoSearchService.infoPageGen(infoSearchService.GetPrompt());

        return "redirect:/infoSearch";
    }
}
