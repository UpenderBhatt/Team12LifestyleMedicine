package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import jakarta.validation.constraints.NotEmpty;

public interface UserService {
    UserEntity registerUser(RegistrationDto registrationDto);

    UserEntity findByEmail(@NotEmpty String email);

    UserEntity findByUsername(@NotEmpty String username);

    UserEntity findById(Long id);
}
