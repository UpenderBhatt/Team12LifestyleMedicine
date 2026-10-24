package uk.ac.cf.cm6213.team12project.riskprofile;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserRepository;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;

@Controller
public class RiskProfileController {

    private final RiskProfileService riskProfileService;
    private final UserRepository userRepository;

    public RiskProfileController(RiskProfileService riskProfileService, UserRepository userRepository) {
        this.riskProfileService = riskProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String ShowProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity currentUser = userRepository.findByUsername(username);

        RiskProfile profile = riskProfileService.getProfile();
        model.addAttribute("profile", profile);
        model.addAttribute("currentUser", currentUser);

        return "riskprofile/profile";
    }
}
