-- V18: Cleanup invalid course offerings (Year mismatches)
-- Removes assignments where the course year does not match the student group year.
-- e.g., Removing Year 3 courses assigned to Year 1 groups.
DELETE FROM course_offerings
WHERE id IN (
        SELECT co.id
        FROM course_offerings co
            JOIN groups g ON co.group_id = g.id
            JOIN courses c ON co.course_id = c.id
        WHERE CAST((c.semester + 1) / 2 AS INT) != g.study_year
    );