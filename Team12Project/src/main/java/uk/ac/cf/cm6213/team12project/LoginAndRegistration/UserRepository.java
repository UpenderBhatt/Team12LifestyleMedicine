package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    boolean existsByUsername(String username);
    UserEntity findFirstByUsername(String username);
}
