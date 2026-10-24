package uk.ac.cf.cm6213.team12project.emailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import uk.ac.cf.cm6213.team12project.emailReminder.emailService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private emailService emailService;

    @BeforeEach
    void assignEmail() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@sender.com");
    }

    @Test
    void sendEmail() {
        String to = "test@email.com";
        String subject = "Test subject";
        String body = "This is a test to check if email service works!";

        emailService.sendEmail(to, subject, body);

        ArgumentCaptor<SimpleMailMessage> messageCaptor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sent = messageCaptor.getValue();

        assertThat(sent.getFrom()).isEqualTo("test@sender.com");
        assertThat(sent.getTo()).containsExactly(to);
        assertThat(sent.getSubject()).isEqualTo(subject);
        assertThat(sent.getText()).isEqualTo(body);
    }
}
