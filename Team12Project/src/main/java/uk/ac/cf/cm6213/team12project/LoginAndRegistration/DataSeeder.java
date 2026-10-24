package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import org.apache.catalina.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role patientRole = Optional.ofNullable(roleRepository.findByName("ROLE_PATIENT")).orElseGet(() -> roleRepository.save(new Role(null, "ROLE_PATIENT", new ArrayList<>())));

        Role clinicianRole = Optional.ofNullable(roleRepository.findByName("ROLE_CLINICIAN")).orElseGet(() -> roleRepository.save(new Role(null, "ROLE_CLINICIAN", new ArrayList<>())));

        Role adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN")).orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN", new ArrayList<>())));

        // Demo account passwords come from environment variables so no real credentials live in the code.
        // If a variable is not set, that demo account is simply not created.
        String clinicianPassword = System.getenv("SEED_CLINICIAN_PASSWORD");
        String adminPassword = System.getenv("SEED_ADMIN_PASSWORD");

        if (clinicianPassword != null && !clinicianPassword.isBlank()
                && userRepository.findByUsername("clinician1") == null) {
            UserEntity clinician = new UserEntity(
                    "clinician1",
                    passwordEncoder.encode(clinicianPassword),
                    "clinician@gmail.com");

            clinician.setRoles(List.of(clinicianRole));
            userRepository.save(clinician);
        }

        if (adminPassword != null && !adminPassword.isBlank()
                && userRepository.findByUsername("admin1") == null) {
            UserEntity admin = new UserEntity(
                    "admin1",
                    passwordEncoder.encode(adminPassword),
                    "admin@gmail.com");

            admin.setRoles(List.of(adminRole));
            userRepository.save(admin);
        }
    }
}
