package uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TaskTests {

    @Test
    void markAsCompleted_setsStatusToCompletedAndLocksTask() {
        task task = new task();

        task.markAsCompleted();

        assertThat(task.getStatus()).isEqualTo(taskStatus.Completed);
        assertThat(task.getLocked()).isTrue();
    }

    @Test
    void markAsCompleted_doesNothingWhenAlreadyLocked() {
        task task = new task();
        task.setStatus(taskStatus.Pending);
        task.setLocked(true);

        task.markAsCompleted();

        assertThat(task.getStatus())
                .as("Status should not change when task is locked")
                .isEqualTo(taskStatus.Pending);
    }

    @Test
    void markAsMissed_setsStatusToMissedAndLocksTask() {
        task task = new task();

        task.markAsMissed();

        assertThat(task.getStatus()).isEqualTo(taskStatus.Missed);
        assertThat(task.getLocked()).isTrue();
    }

    @Test
    void markAsMissed_doesNothingWhenAlreadyLocked() {
        task task = new task();
        task.setStatus(taskStatus.Pending);
        task.setLocked(true);

        task.markAsMissed();

        assertThat(task.getStatus()).isEqualTo(taskStatus.Pending);
    }

    @Test
    void isCompletedAndIsMissed_helpersWorkAsExpected() {
        task task = new task();
        task.setStatus(taskStatus.Completed);
        assertThat(task.isCompleted()).isTrue();
        assertThat(task.isMissed()).isFalse();

        task.setStatus(taskStatus.Missed);
        assertThat(task.isCompleted()).isFalse();
        assertThat(task.isMissed()).isTrue();
    }
}
