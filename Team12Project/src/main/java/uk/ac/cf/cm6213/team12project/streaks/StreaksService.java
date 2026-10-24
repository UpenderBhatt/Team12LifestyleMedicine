package uk.ac.cf.cm6213.team12project.streaks;

import uk.ac.cf.cm6213.team12project.AiImplementation.taskGeneration.task;

import java.time.LocalDate;
import java.util.List;

public interface StreaksService {

    int calculateCurrentStreak(List<task> tasks, LocalDate today);
}
