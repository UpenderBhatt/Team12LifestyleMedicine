-- Query 1: Fetching all tasks for a user, ordered by date
EXPLAIN SELECT * FROM tasks WHERE user_id = 10 ORDER BY task_date ASC;

-- Query 2: Fetching all questionnaire answers for a user, ordered by question
EXPLAIN SELECT * FROM answers WHERE user_id = 10 ORDER BY question_id ASC;

-- Query 3: Fetching user by username
EXPLAIN SELECT * FROM users WHERE username = 'patient10';

-- Query 4: Fetching pending task for a given date
EXPLAIN SELECT * FROM tasks WHERE task_date = '2026-04-29' AND status = 'Pending';