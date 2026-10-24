package uk.ac.cf.cm6213.team12project.goalroadmap.service;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskRepository;

@Service
public class RoadmapVerification {

    private final taskRepository taskRepository;

    public RoadmapVerification(taskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean userOwnsTask(Long userId, Long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> task.getUserId().equals(userId))
                .orElse(false);
    }
}
