package uk.ac.cf.cm6213.team12project.riskprofile;

public class RiskProfile {

    private String gender;
    private String age;
    private String bmi;
    private String healthConditions;
    private String fruitVegServings;
    private String sleepHours;
    private String stressFrequency;
    private String exerciseFrequency;
    private String smokingStatus;
    private String alcoholUse;
    private String goal;
    private String goalReason;
    private String lifestyleConfidence;
    private String readinessToChange;
    private String todaysTask;

    public RiskProfile(
            String gender,
            String age,
            String bmi,
            String healthConditions,
            String fruitVegServings,
            String sleepHours,
            String stressFrequency,
            String exerciseFrequency,
            String smokingStatus,
            String alcoholUse,
            String goal,
            String goalReason,
            String lifestyleConfidence,
            String readinessToChange,
            String todaysTask) {

        this.gender = gender;
        this.age = age;
        this.bmi = bmi;
        this.healthConditions = healthConditions;
        this.fruitVegServings = fruitVegServings;
        this.sleepHours = sleepHours;
        this.stressFrequency = stressFrequency;
        this.exerciseFrequency = exerciseFrequency;
        this.smokingStatus = smokingStatus;
        this.alcoholUse = alcoholUse;
        this.goal = goal;
        this.goalReason = goalReason;
        this.lifestyleConfidence = lifestyleConfidence;
        this.readinessToChange = readinessToChange;
        this.todaysTask = todaysTask;
    }


    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getBmi() {
        return bmi;
    }

    public String getHealthConditions() {
        return healthConditions;
    }

    public String getFruitVegServings() {
        return fruitVegServings;
    }

    public String getSleepHours() {
        return sleepHours;
    }

    public String getStressFrequency() {
        return stressFrequency;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public String getSmokingStatus() {
        return smokingStatus;
    }

    public String getAlcoholUse() {
        return alcoholUse;
    }

    public String getGoal() {
        return goal;
    }

    public String getGoalReason() {
        return goalReason;
    }

    public String getLifestyleConfidence() {
        return lifestyleConfidence;
    }

    public String getReadinessToChange() { return readinessToChange; }

    public String getTodaysTask() { return todaysTask; }


}
