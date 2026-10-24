package uk.ac.cf.cm6213.team12project.AiImplementation.DTOs;

import lombok.Data;

import java.time.LocalDate;

//Generates getters setters and other common functions like to string.
@Data
public class QuestionnaireDto {
    private String name;
    private String gender;
    private Integer age;
    private Double bmi;
    private String healthCondition;
    private String targetGoal;
    private String motivation;
    private Integer confidenceLevel;
    private String goalCategory;
    private String experience;
    private LocalDate startDate;
    private Integer duration;
}
