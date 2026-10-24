package uk.ac.cf.cm6213.team12project.goalroadmap.repo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class StartupData{

    private final ReflectionRepo reflectionRepo;

    public StartupData(ReflectionRepo reflectionRepo) {
        this.reflectionRepo = reflectionRepo;
    }

    @PostConstruct
    @Transactional
    public void resetData() {

        reflectionRepo.deleteAll();
        System.out.println("Data reset: All reflections deleted");
    }
}

