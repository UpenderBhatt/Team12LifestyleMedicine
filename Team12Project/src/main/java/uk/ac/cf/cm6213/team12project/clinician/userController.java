package uk.ac.cf.cm6213.team12project.clinician;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.web.server.ResponseStatusException;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserRepository;
import uk.ac.cf.cm6213.team12project.riskprofile.RiskProfile;
import uk.ac.cf.cm6213.team12project.riskprofile.RiskProfileService;


import java.util.List;
import java.util.stream.Collectors;


@Controller
public class userController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiskProfileService riskProfileService;

    @Autowired
    private ClinicianAccessService clinicianAccessService;


    @GetMapping("/clinician/users")
    public String clinician(Model model) {
        List<UserEntity> users = userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getRoles() != null &&
                        user.getRoles().stream()
                                .anyMatch(role -> "ROLE_PATIENT".equals(role.getName()))
                )
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "clinician/users";
    }


    @GetMapping("/clinician/users/{id}")
    public String viewUser(@PathVariable Long id, Model model, Authentication auth) {
        //If clinician not allowed to access patient data then written forbidden status
        if(!clinicianAccessService.verifyClinicianAccess(auth.getName(),id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        //Return invalid user id if cannot find user in database
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        //Get risk profile for the patient
        RiskProfile profile = riskProfileService.getProfileForUser(id);

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);

        //Returns the patient's risk profile
        return "clinician/user-profile";
    }


    @GetMapping("/clinician/users/{id}/download")
    @ResponseBody
    public ResponseEntity<byte[]> downloadUser(@PathVariable Long id, Authentication auth) throws IOException {
        //If clinician not allowed to access patient data then written forbidden status
        if(!clinicianAccessService.verifyClinicianAccess(auth.getName(),id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //Return invalid user id if cannot find user in database
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        //Generates pdf
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, baos);
        //Writes patients data into pdf and saves it
        document.open();
        document.add(new com.lowagie.text.Paragraph("User Profile"));
        document.add(new com.lowagie.text.Paragraph("ID:" + user.getId()));
        document.add(new com.lowagie.text.Paragraph("Username: " + user.getUsername()));
        document.add(new com.lowagie.text.Paragraph("Email: " + user.getEmail()));
        document.close();

        //Returns successful response with pdf attachment
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user-" + user.getId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }
}