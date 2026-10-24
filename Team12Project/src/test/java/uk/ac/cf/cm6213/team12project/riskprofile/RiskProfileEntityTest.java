package uk.ac.cf.cm6213.team12project.riskprofile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiskProfileEntityTest {

    @Test
    void testRiskProfileConstructorAndGetters() {
        RiskProfile profile = new RiskProfile(
                "Male",          // gender
                "21",            // age
                "23.5",          // bmi
                "None",          // healthConditions
                "3",             // fruitVegServings
                "8",             // sleepHours
                "High",          // stressFrequency
                "4",             // exerciseFrequency
                "Smoker",        // smokingStatus
                "Low",           // alcoholUse
                "Lose weight",   // goal
                "Health",        // goalReason
                "Medium",        // lifestyleConfidence
                "High",          // readinessToChange
                "Walk 30 minutes"// todaysTask
        );

        assertEquals("Male", profile.getGender());
        assertEquals("21", profile.getAge());
        assertEquals("23.5", profile.getBmi());
        assertEquals("None", profile.getHealthConditions());
        assertEquals("3", profile.getFruitVegServings());
        assertEquals("8", profile.getSleepHours());
        assertEquals("High", profile.getStressFrequency());
        assertEquals("4", profile.getExerciseFrequency());
        assertEquals("Smoker", profile.getSmokingStatus());
        assertEquals("Low", profile.getAlcoholUse());
        assertEquals("Lose weight", profile.getGoal());
        assertEquals("Health", profile.getGoalReason());
        assertEquals("Medium", profile.getLifestyleConfidence());
        assertEquals("High", profile.getReadinessToChange());
        assertEquals("Walk 30 minutes", profile.getTodaysTask());
    }
}
