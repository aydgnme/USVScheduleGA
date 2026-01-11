-- V15: Reseed timeslots with clean HH:mm:ss format
-- Clears existing timeslots (and schedule entries if any exist, though checked empty) to ensure specific IDs and formats

DELETE FROM schedule_entries;
DELETE FROM timeslots;

-- Reset Auto Increment if supported (sqlite specific usually requires deleting from sqlite_sequence)
DELETE FROM sqlite_sequence WHERE name='timeslots';

-- Insert Mon-Fri 08:00 - 20:00 (Standard University Day)
-- Monday
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '14:00:00', '16:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '16:00:00', '18:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('MONDAY', '18:00:00', '20:00:00', 120);

-- Tuesday
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '14:00:00', '16:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '16:00:00', '18:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('TUESDAY', '18:00:00', '20:00:00', 120);

-- Wednesday
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '14:00:00', '16:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '16:00:00', '18:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('WEDNESDAY', '18:00:00', '20:00:00', 120);

-- Thursday
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '14:00:00', '16:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '16:00:00', '18:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('THURSDAY', '18:00:00', '20:00:00', 120);

-- Friday
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '14:00:00', '16:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '16:00:00', '18:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('FRIDAY', '18:00:00', '20:00:00', 120);

-- Saturday (Optional, useful for some departments)
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('SATURDAY', '08:00:00', '10:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('SATURDAY', '10:00:00', '12:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('SATURDAY', '12:00:00', '14:00:00', 120);
INSERT INTO timeslots (day_of_week, start_time, end_time, duration_minutes) VALUES ('SATURDAY', '14:00:00', '16:00:00', 120);
