package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool.Impl;

import java.util.*;
import uk.ac.cf.cm6213.team12project.AiImplementation.DTOs.QuestionnaireDto;


public class BuildPrompt {

    public String buildPrompt(QuestionnaireDto userInfo) {
        StringBuilder prompt  = new StringBuilder();

        prompt.append("I want you to fill out the appended html doc but so that it is fitting to this clients goals and needs, ensure all the information comes from reputable sources.");
        
        prompt.append("Pateint Info:");

        if(userInfo.getName()!=null){
            prompt.append(" Name: "+userInfo.getName()).append("\n");
        }
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
            prompt.append("Exercise experience = ").append(userInfo.getExperience()).append("\n");
        }
        if(userInfo.getTargetGoal() != null){
            prompt.append("Goal = ").append(userInfo.getTargetGoal()).append("\n");
        }
        if(userInfo.getMotivation() != null){
            prompt.append("Motivation to complete goal out of 10 = ").append(userInfo.getMotivation()).append("\n");
        }
        if(userInfo.getConfidenceLevel() != null){
            prompt.append("Confidence to complete goal out of 10 = ").append(userInfo.getConfidenceLevel()).append("\n");
        }

        prompt.append("\nhtml doc: ");
        prompt.append("""

                <div class="container">
                
                    <header>
                
                        <h1>Hello <username>!</h1>
                
                        <p class="welcome-message">
                
                            Welcome to your personalised health and fitness info page. We've curated resources to help you safely achieve your goal of <bold>losing 10kg</bold>, especially while managing <bold>diabetes</bold> and starting with <bold>beginner</bold> experience.
                
                        </p>
                
                    </header>
                
                
                
                    <section class="info-section">
                
                        <h2>Your Health & Wellness Priorities</h2>
                
                        <ul>
                
                            <li>🩸 <bold>Diabetes Management:</bold> Before starting any new exercise or diet, <bold>consult your doctor or diabetes care team</bold>. Regular exercise can improve insulin sensitivity, but you must monitor your blood sugar closely, especially when starting out.</li>
                
                            <li>⚖️ <bold>Weight Loss:</bold> A safe and sustainable goal is to lose around 0.5kg to 1kg (1lb to 2lb) per week. Focus on <bold>small, consistent changes</bold> to diet and exercise.</li>
                
                            <li>🚶 <bold>Starting Exercise:</bold> As a beginner, focus on <bold>walking</bold>. Aim for a brisk 30-minute walk 5 times a week. This is low-impact and highly effective for health and weight management.</li>
                
                            <li>🥗 <bold>Nutrition:</bold> Losing weight is primarily achieved through diet. Focus on consuming a balanced diet rich in vegetables, lean proteins, and whole grains, keeping your diabetes in mind.</li>
                
                        </ul>
                
                    </section>
                
                
                
                    <section class="info-section">
                
                        <h2>Understanding BMI and Health</h2>
                
                        <p>Your BMI of 30 falls into the obese category. Losing your goal of 10kg is a great step that will significantly reduce your risk of related health issues and improve your diabetes management.</p>
                
                
                
                        <div class="visual-guide">
                
                
                
                            <p class="caption">The BMI chart helps assess general health risk. Moving down a category has significant health benefits.</p>
                
                        </div>
                
                    </section>
                
                
                
                    <section class="resource-section">
                
                        <h2>Trusted Resources (NHS & UK Health)</h2>
                
                        <div class="resource-grid">
                
                
                
                            <a href="https://www.nhs.uk/conditions/diabetes/" target="_blank" class="resource-card">
                
                                <h3>NHS Diabetes Guide</h3>
                
                                <p>Official information on living with diabetes, including diet and exercise advice.</p>
                
                                <span class="link-text">Visit NHS Site &rarr;</span>
                
                            </a>
                
                
                
                            <a href="https://www.nhs.uk/better-health/lose-weight/" target="_blank" class="resource-card">
                
                                <h3>NHS Weight Loss Plan</h3>
                
                                <p>The free 12-week plan provides a safe, simple structure for diet and activity to help you lose weight.</p>
                
                                <span class="link-text">View 12-Week Plan &rarr;</span>
                
                            </a>
                
                
                
                            <a href="https://www.diabetes.org.uk/guide-to-diabetes/managing-your-diabetes/exercise" target="_blank" class="resource-card">
                
                                <h3>Diabetes UK: Exercise Safely</h3>
                
                                <p>Essential advice on how to monitor blood sugar, manage medication, and exercise with diabetes.</p>
                
                                <span class="link-text">Read Safety Guide &rarr;</span>
                
                            </a>
                
                
                
                            <a href="https://www.nhs.uk/live-well/exercise/easy-ways-to-get-more-active/" target="_blank" class="resource-card">
                
                                <h3>NHS Easy Ways to be Active</h3>
                
                                <p>Low-impact, beginner-friendly activities to build confidence and fitness consistency.</p>
                
                                <span class="link-text">Find Activities &rarr;</span>
                
                            </a>
                
                
                
                        </div>
                
                    </section>
                
                
                
                    <footer>
                
                        <p>Powered by NHS and other resources.</p>
                
                        <p><em><bold>Always consult your GP or diabetes care team before starting a new intense exercise or diet regime.</bold></em></p>
                
                    </footer>
                
                </div>
                
                """);

        prompt.append("Do not return anything other than the output.");

        return prompt.toString();
    }


}
