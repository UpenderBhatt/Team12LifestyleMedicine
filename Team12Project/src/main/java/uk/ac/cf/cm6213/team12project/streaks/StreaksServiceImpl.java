package uk.ac.cf.cm6213.team12project.streaks;

import org.springframework.stereotype.Service;
import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class StreaksServiceImpl implements StreaksService {

    @Override
    public int calculateCurrentStreak(List<task> tasks, LocalDate today) {
        if (tasks == null || tasks.isEmpty() || today == null) {
            return 0;
        }

        // Copy + sort by date (oldest → newest)
        List<task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(task::getTaskDate));

        int streak = 0;

        for (task task : sortedTasks) {
            LocalDate date = task.getTaskDate();

            // Ignore future days completely (grey tiles)
            if (date.isAfter(today)) {
                break;
            }

            boolean completed = task.isCompleted();

            if (date.isBefore(today)) {
                // PAST DAY:
                //  - green  => extend streak
                //  - red/pending => break streak
                if (completed) {
                    streak++;
                } else {
                    streak = 0;
                }
            } else {
                // TODAY:
                //  - if completed (green) it should extend streak
                //  - if NOT completed (blue tile) it should NOT break the streak yet
                if (completed) {
                    streak++;
                }
                // else: leave streak as it is
            }
        }

        return streak;
    }
}
