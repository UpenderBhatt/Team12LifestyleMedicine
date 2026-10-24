package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskRepository;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskStatus;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestDataSeeder implements CommandLineRunner {

    private static final int NUM_PATIENTS = 50;
    private static final int TASKS_PER_PATIENT = 42;
    private static final int ANSWERS_PER_PATIENT = 10;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final taskRepository taskRepository;
    private final AnswerRepository answerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.testdata:false}")
    private boolean seedEnabled;

    public TestDataSeeder(UserRepository userRepository, RoleRepository roleRepository, taskRepository taskRepository, AnswerRepository answerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
        this.answerRepository = answerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if(!seedEnabled) {
            return;
        }

        if (userRepository.findByUsername("patient50") != null) {
            System.out.println("Test data already exists");
            return;
        }

        Role patientRole = roleRepository.findByName("ROLE_PATIENT");
        if(patientRole == null) {
            patientRole = roleRepository.save(new Role(null, "ROLE_PATIENT", new ArrayList<>()));
        }

        long start = System.currentTimeMillis();
        String hashedPassword = passwordEncoder.encode("password");
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= NUM_PATIENTS; i++) {
            UserEntity user = new UserEntity("patient" + i, hashedPassword, "patient" + i + "@test.com");
            user.setRoles(List.of(patientRole));
            UserEntity savedUser = userRepository.save(user);
            Long userId = savedUser.getId();

            List<task> tasks = new ArrayList<>(TASKS_PER_PATIENT);
            for (int day = 0; day < TASKS_PER_PATIENT; day++) {
                task t = new task();
                t.setUserId(userId);
                t.setTaskTitle("Daily activity " + (day + 1));
                t.setTaskDescription("Walk for 30 minutes");
                t.setTaskDate(today.plusDays(day));
                t.setStatus(day < 7 ? taskStatus.Completed : taskStatus.Pending);
                tasks.add(t);
            }
            taskRepository.saveAll(tasks);

            List<Answer> answers = new ArrayList<>(ANSWERS_PER_PATIENT);
            for (int q = 1; q <= ANSWERS_PER_PATIENT; q++) {
                Answer a = new Answer("Sample answer " + q, now, (long) q);
                a.setUserId(userId);
                answers.add(a);
            }
            answerRepository.saveAll(answers);
        }

        System.out.println("Data seeded successfully!");
    }
}
