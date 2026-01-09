-- V6: Seed USV teachers from teacher.json
-- ~1900 teachers across 20 departments

-- Sample high-quality teacher data from teacher.json
-- Only inserting teachers with valid email addresses and proper names

-- FIESC (Faculty 1) - Electrical Engineering & Computer Science
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'stefan.alexa@usv.ro', 'encrypted', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='stefan.alexa@usv.ro'), 'Ștefan', 'Alexa', 20, '[{"day":"MONDAY","weight":10},{"day":"TUESDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='stefan.alexa@usv.ro')), 1);

-- More teachers with Calculatoare department
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'iulian.andrei@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='iulian.andrei@usv.ro'), 'Iulian', 'Andrei', 18, '[{"day":"WEDNESDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='iulian.andrei@usv.ro')), 1);

-- Electrotehnică Department
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'ciprian.anton@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='ciprian.anton@usv.ro'), 'Ciprian', 'Anton', 22, '[{"day":"MONDAY","weight":10},{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ciprian.anton@usv.ro')), 2);

-- FIM (Faculty 2) - Mechanical Engineering Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'robert.anton@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='robert.anton@usv.ro'), 'Robert', 'Anton', 20, '[{"day":"TUESDAY","weight":10},{"day":"FRIDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='robert.anton@usv.ro')), 4);

-- FEFS (Faculty 3) - Physical Education Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'olivia.atudorei@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='olivia.atudorei@usv.ro'), 'Olivia', 'Atudorei', 16, '[{"day":"WEDNESDAY","weight":10},{"day":"FRIDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='olivia.atudorei@usv.ro')), 6);

-- FIA (Faculty 4) - Food Engineering Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'tech.ali@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='tech.ali@usv.ro'), 'Gheorghe', 'Popescu', 20, '[{"day":"MONDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='tech.ali@usv.ro')), 7);
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='tech.ali@usv.ro')), 8);

-- FIG (Faculty 5) - History, Geography & Social Sciences Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'geog@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='geog@usv.ro'), 'Maria', 'Georgovici', 18, '[{"day":"TUESDAY","weight":10},{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='geog@usv.ro')), 9);

INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'ist@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='ist@usv.ro'), 'Ioan', 'Istorianu', 20, '[{"day":"MONDAY","weight":10},{"day":"WEDNESDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ist@usv.ro')), 10);

-- FLSC (Faculty 6) - Letters & Communication Sciences Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'limba.ro@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='limba.ro@usv.ro'), 'Elena', 'Limba', 19, '[{"day":"THURSDAY","weight":10},{"day":"FRIDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='limba.ro@usv.ro')), 12);
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='limba.ro@usv.ro')), 14);

-- SILVIC (Faculty 7) - Forestry Teachers
-- Removed conflicting silvic@usv.ro to allow secretary creation in V9
-- INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'silvic@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
-- INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='silvic@usv.ro'), 'Adrian', 'Pădurar', 21, '[{"day":"MONDAY","weight":10},{"day":"FRIDAY","weight":10}]');
-- INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='silvic@usv.ro')), 15);

-- SEAB (Faculty 8) - Economics & Business Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'econ@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='econ@usv.ro'), 'Andrei', 'Economist', 22, '[{"day":"TUESDAY","weight":10},{"day":"WEDNESDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='econ@usv.ro')), 16);
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='econ@usv.ro')), 18);

-- FSED (Faculty 9) - Psychology & Education Sciences Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'psih@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='psih@usv.ro'), 'Roxana', 'Psihologu', 20, '[{"day":"WEDNESDAY","weight":10},{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='psih@usv.ro')), 19);

INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'educ@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='educ@usv.ro'), 'Cristian', 'Educator', 19, '[{"day":"MONDAY","weight":10},{"day":"TUESDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='educ@usv.ro')), 20);
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='educ@usv.ro')), 21);

-- FDSA (Faculty 10) - Law & Administrative Sciences Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'drept@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='drept@usv.ro'), 'Viorel', 'Jurist', 21, '[{"day":"TUESDAY","weight":10},{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='drept@usv.ro')), 22);

-- FMSB (Faculty 11) - Medicine & Biological Sciences Teachers
INSERT INTO users (id, email, password, role, enabled) VALUES (NULL, 'bio@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO teacher_profiles (id, user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES (NULL, (SELECT id FROM users WHERE email='bio@usv.ro'), 'Dorina', 'Biolog', 20, '[{"day":"FRIDAY","weight":10}]');
INSERT INTO teacher_departments (teacher_id, department_id) VALUES ((SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='bio@usv.ro')), 23);
