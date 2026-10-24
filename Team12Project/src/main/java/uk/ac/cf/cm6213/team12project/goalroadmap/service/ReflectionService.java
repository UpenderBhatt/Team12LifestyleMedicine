package uk.ac.cf.cm6213.team12project.goalroadmap.service;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.goalroadmap.repo.ReflectionRepo;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReflectionService {

    private final ReflectionRepo reflectionRepo;

    public ReflectionService(ReflectionRepo reflectionRepo) {
        this.reflectionRepo = reflectionRepo;
    }

    public ReflectionInst saveOrUpdateReflection(Long userId, Long taskId, String content) {
        Optional<ReflectionInst> existingReflection = reflectionRepo.findByTaskId(taskId);

        if (existingReflection.isPresent()) {
            ReflectionInst reflection = existingReflection.get();
            reflection.setContent(content);
            reflection.setDateWritten(LocalDate.now());
            return reflectionRepo.save(reflection);
        }

        ReflectionInst newReflection = new ReflectionInst(userId, taskId, content);
        return reflectionRepo.save(newReflection);
    }

    public Optional<ReflectionInst> getReflectionByTask(Long taskId) {
        return reflectionRepo.findByTaskId(taskId);
    }
}
