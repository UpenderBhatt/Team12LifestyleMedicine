package uk.ac.cf.cm6213.team12project.LoginReistrationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.*;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.Impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegistrationDto registrationDto;
    private Role patientRole;

    @BeforeEach
    void setUp() {
        registrationDto = new RegistrationDto();
        registrationDto.setUsername("fred");
        registrationDto.setEmail("test@test.com");
        registrationDto.setPassword("passwrod");

        patientRole = new Role();
        patientRole.setName("ROLE_PATIENT");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        when(roleRepository.findByName("ROLE_PATIENT")).thenReturn(patientRole);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");

        UserEntity savedUserMock = new UserEntity("fred", "encodedPassword", "test@test.com");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserMock);

        // Act
        UserEntity result = userService.registerUser(registrationDto);

        // Assert
        assertNotNull(result);
        assertEquals("fred", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        // Verify interactions
        verify(passwordEncoder).encode("passwrod");
        verify(roleRepository).findByName("ROLE_PATIENT");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByEmail() {
        // Arrange
        String email = "Test@tesg.test";
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act
        UserEntity result = userService.findByEmail(email);

        // Assert
        assertEquals(email, result.getEmail());
    }
}
