package uk.ac.cf.cm6213.team12project.clinician;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserRepository;

@Service
public class ClinicianAccessService {
    private final UserRepository userRepository;

    public ClinicianAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Function that checks if the clinician should be able to access the patient information
    public boolean verifyClinicianAccess(String clinicianUsername, Long patientID){
        UserEntity patient = userRepository.findById(patientID).orElse(null);

        //Checks if patient exists in database
        if(patient == null){
            return false;
        }
        else{
            return true;
        }
    }
}
