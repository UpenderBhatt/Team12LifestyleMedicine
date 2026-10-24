package uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.questionnaire.Answer;
import uk.ac.cf.cm6213.team12project.questionnaire.AnswerRepository;
import uk.ac.cf.cm6213.team12project.AiImplementation.DTOs.QuestionnaireDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

@Service
public class taskGenService {

    private final Client aiClient;
    private static final String modelId = "gemini-2.5-flash";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final taskRepository taskRepository;
    private final AnswerRepository answerRepository;
    private int dateOffset = 0;

    public taskGenService(Client aiClient, taskRepository taskRepository, AnswerRepository answerRepository) {
        this.aiClient = aiClient;
        this.taskRepository = taskRepository;
        this.answerRepository = answerRepository;
    }

    private String sanitizeJson(String text) {
        text = text.trim();

        if (text.startsWith("```")) {
            text = text.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();
        }

        int firstBrace = text.indexOf("{");
        int lastBrace = text.lastIndexOf("}");

        if (firstBrace != -1 && lastBrace != -1) {
            return text.substring(firstBrace, lastBrace + 1);
        }

        return text;
    }

    public List<generatedTask> generateTasks(String prompt) {

        GenerateContentResponse response = aiClient.models.generateContent(modelId, prompt, null);

        String text = response.text();
        text = sanitizeJson(text);

        try{
            JsonNode root = objectMapper.readTree(text);
            JsonNode tasksNode = root.get("tasks");

            List<generatedTask> generatedTasks = new ArrayList<>();
            if(tasksNode != null && tasksNode.isArray()){
                for (JsonNode taskNode : tasksNode){
                    generatedTask task = new generatedTask();
                    task.setTitle(taskNode.get("Title").asText());
                    task.setDescription(taskNode.get("Description").asText());
                    generatedTasks.add(task);
                }
            }
            return generatedTasks;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Failed to parse response", e);
        }

    }

    private String buildPrompt(QuestionnaireDto userInfo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a clinician who helps people with lifestyle changes and you have experience as a fitness coach. Create a progressive exercise only plan for a patient.\n");
        prompt.append("Pateint Info:\n");

        if(userInfo.getGender() != null){
            prompt.append("Gender = ").append(userInfo.getGender()).append("\n");
        }
        if(userInfo.getAge() != null){
            prompt.append("Age = ").append(userInfo.getAge()).append("\n");
        }
        if(userInfo.getBmi() != null){
            prompt.append("BMI = ").append(userInfo.getBmi()).append("\n");
        }
        if(userInfo.getHealthCondition() != null){
            prompt.append("Health Conditions = ").append(userInfo.getHealthCondition()).append("\n");
        }
        if(userInfo.getExperience() != null){
            prompt.append("Experience = ").append(userInfo.getExperience()).append("\n");
        }
        if(userInfo.getTargetGoal() != null){
            prompt.append("Goal = ").append(userInfo.getTargetGoal()).append("\n");
        }
        if(userInfo.getMotivation() != null){
            prompt.append("Motivation = ").append(userInfo.getMotivation()).append("\n");
        }
        if(userInfo.getConfidenceLevel() != null){
            prompt.append("Confidence out of 10 = ").append(userInfo.getConfidenceLevel()).append("\n");
        }
        if(userInfo.getGoalCategory() != null){
            prompt.append("Goal category = ").append(userInfo.getGoalCategory()).append("\n");
        }
        if(userInfo.getDuration() != null){
            prompt.append("Plan duration in days = ").append(userInfo.getDuration()).append("\n");
        }

        prompt.append("You are only allowed to set 1 main task per day.\n" +
                "\n" +
                "Return a JSON object with a \"tasks\" array, where each item has:\n" +
                "\n" +
                "Title - short name for the task\n" +
                "Description - short description of what the task is\n" +
                "\n" +
                "Only output valid JSON. No extra text.");

        return prompt.toString();
    }

    @Transactional
    public void setTasks(Long userId) {
        QuestionnaireDto userInfo = getTaskGenInfo(userId);

        String prompt = buildPrompt(userInfo);
        List<generatedTask> generatedTasks = generateTasks(prompt);

        AtomicInteger dayOffSet = new AtomicInteger(0);
        List<task> tasks = generatedTasks.stream().map(dto -> {
            task task = new task();
            task.setUserId(userId);
            task.setTaskTitle(dto.getTitle());
            task.setTaskDescription(dto.getDescription());
            task.setTaskDate(LocalDate.now().plusDays(dayOffSet.getAndIncrement()));
            task.setStatus(taskStatus.Pending);
            task.setLocked(false);
            return task;
        }).toList();
        taskRepository.saveAll(tasks);
    }

    public List<task> getTasksForUser(Long userId) {
     return taskRepository.findByUserIdOrderByTaskDateAsc(userId);
    }

    public int getDateOffset() {
        return dateOffset;
    }

    public LocalDate getCurrentSimulatedDate() {
        return LocalDate.now().plusDays(dateOffset);
    }

    public void nextDay() {
        dateOffset++;
        updateExpiredTasks();
    }

    public void previousDay() {
        dateOffset--;
    }

    public List<task> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(task::getTaskDate))
                .toList();
    }

    public void updateTaskStatus(Long taskId, taskStatus status) {
        task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void updateExpiredTasks() {
        LocalDate today = getCurrentSimulatedDate();

        List<task> tasks = taskRepository.findAll();

        for (task task : tasks) {

            if (task.getTaskDate().isAfter(today)) {
                continue;
            }

            if ((task.getStatus() == taskStatus.Pending) &&
                    task.getTaskDate().isBefore(today)) {

                task.setStatus(taskStatus.Missed);
                taskRepository.save(task);
            }
        }
    }

    private QuestionnaireDto getTaskGenInfo(Long userId) {
        List<Answer> answers = answerRepository.findByUserIdOrderByQuestionIdAsc(userId);

        Map<Long, String> byQuestionId = answers.stream()
                .collect(Collectors.toMap(
                        Answer::getQuestionId,
                        Answer::getResponse,
                        (a, b) -> a
                ));

        validateQuestionnaireAnswers(byQuestionId);

        QuestionnaireDto taskGenInfo = new QuestionnaireDto();

        taskGenInfo.setGender(byQuestionId.get(1L));
        taskGenInfo.setAge(Integer.parseInt(byQuestionId.get(2L)));
        taskGenInfo.setBmi(parseDouble(byQuestionId.get(3L)));
        taskGenInfo.setHealthCondition(byQuestionId.get(4L));
        taskGenInfo.setExperience(byQuestionId.get(8L));
        taskGenInfo.setTargetGoal(byQuestionId.get(11L));
        taskGenInfo.setMotivation(byQuestionId.get(12L));
        taskGenInfo.setConfidenceLevel(Integer.parseInt(byQuestionId.get(13L)));
        taskGenInfo.setGoalCategory("General fitness");
        taskGenInfo.setStartDate(LocalDate.now());
        taskGenInfo.setDuration(42);

        return taskGenInfo;
    }


    private void validateQuestionnaireAnswers(Map<Long, String> byQuestionId) {
        if (!byQuestionId.containsKey(1L) ||
                !byQuestionId.containsKey(2L) ||
                !byQuestionId.containsKey(3L) ||
                !byQuestionId.containsKey(4L) ||
                !byQuestionId.containsKey(8L) ||
                !byQuestionId.containsKey(11L) ||
                !byQuestionId.containsKey(12L) ||
                !byQuestionId.containsKey(13L)) {
            throw new InvalidQuestionnaireData("Missing questionnaire answers!");
        }

        String gender = byQuestionId.get(1L);
        if (gender == null || gender.trim().isEmpty()) {
            throw new InvalidQuestionnaireData("Gender can not be empty.");
        }

        try {
            int age = Integer.parseInt(byQuestionId.get(2L));
            if (age < 16 || age > 150) {
                throw new InvalidQuestionnaireData("Age must be between 16 and 150.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidQuestionnaireData("Age must be a valid number.");
        }

        try {
            double bmi = Double.parseDouble(byQuestionId.get(3L));
            if (bmi < 10.0 || bmi > 60.0) {
                throw new InvalidQuestionnaireData("BMI must be between 10 and 80.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidQuestionnaireData("BMI must be a valid number between 10 and 80.");
        }

        try {
            int confidence = Integer.parseInt(byQuestionId.get(13L));
            if (confidence < 1 || confidence > 10) {
                throw new InvalidQuestionnaireData("Confidence must be between 1 and 10.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidQuestionnaireData("Confidence must be a valid number between 1 and 10.");
        }
    }
}
