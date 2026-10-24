package uk.ac.cf.cm6213.team12project.goalroadmap.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserEntity;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.UserService;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionInst;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionService;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.RoadmapVerification;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskGenService;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.taskStatus;
import uk.ac.cf.cm6213.team12project.streaks.StreaksService;

import java.time.LocalDate;
import java.util.*;

@Controller
public class RoadmapController {

    private final ReflectionService reflectionService;
    private final UserService userService;
    private final taskGenService taskService;
    private final RoadmapVerification roadmapVerification;
    private final StreaksService streaksService;

    public RoadmapController(
            ReflectionService reflectionService,
            UserService userService,
            taskGenService taskService,
            RoadmapVerification roadmapVerification,
            StreaksService streaksService

    ) {
        this.reflectionService = reflectionService;
        this.userService = userService;
        this.taskService = taskService;
        this.roadmapVerification = roadmapVerification;
        this.streaksService = streaksService;
    }

    // Security Auth
    private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByUsername(auth.getName());
        return user.getId();
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public String roadmap(Model model) {

        Long userId = getLoggedInUserId();
        UserEntity user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<task> tasks = taskService.getTasksForUser(userId);

        // Calculates new streak
        LocalDate todayDate = taskService.getCurrentSimulatedDate();
        int currentStreak = streaksService.calculateCurrentStreak(tasks, todayDate);

        List<List<task>> weeks = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i += 7) {
            int end = Math.min(i + 7, tasks.size());
            weeks.add(tasks.subList(i, end));
        }

        model.addAttribute("weeks", weeks);
        model.addAttribute("userId", userId);
        model.addAttribute("todayDate", taskService.getCurrentSimulatedDate());
        model.addAttribute("dateOffset", taskService.getDateOffset());

        model.addAttribute("currentStreak", currentStreak);

        return "goal-roadmap";
    }

    @GetMapping("/api/reflections/task/{taskId}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> getReflection(@PathVariable Long taskId) {

        Long userId = getLoggedInUserId();
        Map<String, Object> response = new HashMap<>();

        // IDOR protection: ensures it is the users task
        if (!roadmapVerification.userOwnsTask(userId, taskId)) {
            response.put("error", "Unauthorised access");
            return response;
        }

        reflectionService.getReflectionByTask(taskId).ifPresentOrElse(
                reflection -> {
                    response.put("exists", true);
                    response.put("content", reflection.getContent());
                },
                () -> response.put("exists", false)
        );

        return response;
    }

    @PostMapping("/api/reflections/save")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> saveReflection(@RequestBody ReflectionRequest req) {

        Long userId = getLoggedInUserId();
        Map<String, Object> response = new HashMap<>();

        // IDOR check 1 = cannot use another user's ID
        if (!userId.equals(req.userId())) {
            response.put("error", "Unauthorised user");
            return response;
        }

        // IDOR check 2 = cannot write reflection for someone else's goal
        if (!roadmapVerification.userOwnsTask(userId, req.taskId())) {
            response.put("error", "Unauthorised task access");
            return response;
        }

        ReflectionInst reflection = reflectionService.saveOrUpdateReflection(
                req.userId(),
                req.taskId(),
                req.content()
        );

        response.put("success", true);
        response.put("reflectionId", reflection.getId());
        return response;
    }

    @PostMapping("/api/tasks/{taskId}/status")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> body
    ) {
        Long userId = getLoggedInUserId();
        Map<String, Object> response = new HashMap<>();

        // IDOR Security
        if (!roadmapVerification.userOwnsTask(userId, taskId)) {
            response.put("error", "Unauthorised task update");
            return response;
        }

        taskService.updateTaskStatus(taskId, taskStatus.Completed);

        response.put("success", true);
        return response;
    }

    @GetMapping("/test/next-day")
    @ResponseBody
    public String testNextDay() {
        taskService.nextDay();
        return "Moved forward one day. Current simulated date: " + taskService.getCurrentSimulatedDate();
    }

    @GetMapping("/test/previous-day")
    @ResponseBody
    public String testPreviousDay() {
        taskService.previousDay();
        return "Moved backward one day. Current simulated date: " + taskService.getCurrentSimulatedDate();
    }

    public record ReflectionRequest(Long userId, Long taskId, String content) {}
}
