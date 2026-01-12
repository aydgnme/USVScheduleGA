-- V15: Replicate Course Offerings from C-001 to all Year 1 Calculatoare groups
-- This ensures that C-002 through C-008 have the same courses/teachers assigned as C-001.
INSERT INTO course_offerings (
        course_id,
        teacher_id,
        group_id,
        weekly_hours,
        parity
    )
SELECT co.course_id,
    co.teacher_id,
    g.id,
    co.weekly_hours,
    co.parity
FROM course_offerings co
    JOIN groups source_group ON co.group_id = source_group.id
    CROSS JOIN groups g
WHERE source_group.code = 'C-001'
    AND g.specialization_id = 3 -- Calculatoare
    AND g.study_year = 1
    AND g.code LIKE 'C-%' -- Safety check
    AND g.code != 'C-001' -- Don't insert for source
    -- Ensure we don't insert duplicate if it already exists (though V11 ran once)
    AND NOT EXISTS (
        SELECT 1
        FROM course_offerings existing
        WHERE existing.course_id = co.course_id
            AND existing.group_id = g.id
            AND existing.teacher_id = co.teacher_id
    );