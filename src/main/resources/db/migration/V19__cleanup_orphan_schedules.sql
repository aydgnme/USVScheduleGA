-- V19: Cleanup orphan schedule entries
-- V18 deleted some course_offerings but schedule_entries might still reference them.
-- This cleans up those broken references.
DELETE FROM schedule_entries
WHERE offering_id NOT IN (
        SELECT id
        FROM course_offerings
    );