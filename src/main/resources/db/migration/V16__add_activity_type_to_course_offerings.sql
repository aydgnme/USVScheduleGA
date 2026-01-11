-- Add activity_type column to course_offerings
ALTER TABLE course_offerings ADD COLUMN activity_type TEXT;

-- Populate activity_type from the associated course's component_type
UPDATE course_offerings
SET activity_type = (
    SELECT component_type
    FROM courses
    WHERE courses.id = course_offerings.course_id
);
