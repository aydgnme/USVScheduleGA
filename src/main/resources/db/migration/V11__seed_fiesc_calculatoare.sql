-- V11: Seed FIESC Calculatoare Professors and Courses (Years 1-4)
-- Adds 10 Professors, ~32 Courses (Romanian), and Allocations

-- 1. Insert Users (Teachers)
INSERT INTO users (email, password, role, enabled) VALUES ('ion.popescu@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('vasile.ionescu@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('maria.dumitrescu@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('elena.radu@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('gheorghe.stan@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('ana.stoica@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('constantin.gheorghiu@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('mihai.matei@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('florin.nistor@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);
INSERT INTO users (email, password, role, enabled) VALUES ('roxana.dobre@usv.ro', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3rfMX5j01k0gPMkarma0CRZnP3eLMDm', 'TEACHER', 1);

-- 2. Insert Teacher Profiles
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='ion.popescu@usv.ro'), 'Ion', 'Popescu', 20, '[{"day":"MONDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='vasile.ionescu@usv.ro'), 'Vasile', 'Ionescu', 20, '[{"day":"TUESDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='maria.dumitrescu@usv.ro'), 'Maria', 'Dumitrescu', 18, '[{"day":"WEDNESDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='elena.radu@usv.ro'), 'Elena', 'Radu', 22, '[{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='gheorghe.stan@usv.ro'), 'Gheorghe', 'Stan', 20, '[{"day":"FRIDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='ana.stoica@usv.ro'), 'Ana', 'Stoica', 16, '[{"day":"MONDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='constantin.gheorghiu@usv.ro'), 'Constantin', 'Gheorghiu', 20, '[{"day":"TUESDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='mihai.matei@usv.ro'), 'Mihai', 'Matei', 24, '[{"day":"WEDNESDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='florin.nistor@usv.ro'), 'Florin', 'Nistor', 20, '[{"day":"THURSDAY","weight":10}]');
INSERT INTO teacher_profiles (user_id, first_name, last_name, max_hours_weekly, available_days_json) VALUES ((SELECT id FROM users WHERE email='roxana.dobre@usv.ro'), 'Roxana', 'Dobre', 20, '[{"day":"FRIDAY","weight":10}]');

-- 3. Link Teachers to Department (Calculatoare - ID 1)
INSERT INTO teacher_departments (teacher_id, department_id) SELECT id, 1 FROM teacher_profiles WHERE user_id IN (SELECT id FROM users WHERE email IN ('ion.popescu@usv.ro', 'vasile.ionescu@usv.ro', 'maria.dumitrescu@usv.ro', 'elena.radu@usv.ro', 'gheorghe.stan@usv.ro', 'ana.stoica@usv.ro', 'constantin.gheorghiu@usv.ro', 'mihai.matei@usv.ro', 'florin.nistor@usv.ro', 'roxana.dobre@usv.ro'));

-- 4. Insert Courses (Years 1-4)
-- Year 1
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-101', 'Algebră liniară, geometrie analitică', 'LECTURE', 5, 1, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-102', 'Analiză matematică', 'LECTURE', 5, 1, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-103', 'Proiectare logică', 'LECTURE', 6, 1, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-104', 'Programarea calculatoarelor', 'LECTURE', 6, 1, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-105', 'Structuri de date şi algoritmi', 'LECTURE', 6, 2, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-106', 'Arhitectura sistemelor de calcul', 'LECTURE', 5, 2, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-107', 'Electrotehnică', 'LECTURE', 5, 2, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-108', 'Fizică I', 'LECTURE', 4, 1, 'BOTH');

-- Year 2
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-201', 'Programare Orientată pe Obiecte', 'LECTURE', 6, 3, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-202', 'Teoria sistemelor', 'LECTURE', 5, 3, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-203', 'Dispozitive electronice', 'LECTURE', 5, 3, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-204', 'Metode numerice', 'LECTURE', 4, 3, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-205', 'Reţele de calculatoare', 'LECTURE', 6, 4, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-206', 'Structura şi organizarea calculatoarelor', 'LECTURE', 5, 4, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-207', 'Electronică digitală', 'LECTURE', 5, 4, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-208', 'Programarea interfeţelor utilizator', 'LECTURE', 4, 4, 'BOTH');

-- Year 3
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-301', 'Sisteme de operare', 'LECTURE', 6, 5, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-302', 'Baze de date', 'LECTURE', 6, 5, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-303', 'Proiectarea algoritmilor', 'LECTURE', 5, 5, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-304', 'Protocoale de comunicaţii', 'LECTURE', 5, 5, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-305', 'Inteligenţă artificială', 'LECTURE', 6, 6, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-306', 'Microcontrolere', 'LECTURE', 5, 6, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-307', 'Elemente de grafică pe calculator', 'LECTURE', 5, 6, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-308', 'Proiectarea aplicațiilor WEB', 'LECTURE', 4, 6, 'BOTH');

-- Year 4
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-401', 'Ingineria programelor', 'LECTURE', 6, 7, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-402', 'Sisteme distribuite', 'LECTURE', 5, 7, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-403', 'Sisteme inteligente', 'LECTURE', 5, 7, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-404', 'Criptografie şi securitate informaţională', 'LECTURE', 5, 8, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-405', 'Calcul mobil', 'LECTURE', 4, 8, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-406', 'Internetul obiectelor', 'LECTURE', 4, 8, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-407', 'Sisteme de calcul în timp real', 'LECTURE', 5, 8, 'BOTH');
INSERT INTO courses (code, title, component_type, credits, semester, parity) VALUES ('C-408', 'Arhitecturi şi prelucrări paralele', 'LECTURE', 5, 8, 'BOTH');

-- 5. Insert Course Offerings (Allocations - 3 courses minimum per teacher)
-- Group assignment logic: Assign to first C group 'C-001' (simplification)
-- Prof 1: Ion Popescu (Programming/Math)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-101'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ion.popescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-104'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ion.popescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-201'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ion.popescu@usv.ro')), 2, 'BOTH');

-- Prof 2: Vasile Ionescu (Math/Theory)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-102'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='vasile.ionescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-202'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='vasile.ionescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-204'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='vasile.ionescu@usv.ro')), 2, 'BOTH');

-- Prof 3: Maria Dumitrescu (Hardware/Logic)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-103'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='maria.dumitrescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-106'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='maria.dumitrescu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-206'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='maria.dumitrescu@usv.ro')), 2, 'BOTH');

-- Prof 4: Elena Radu (Data/Algo/DB)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-105'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='elena.radu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-302'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='elena.radu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-303'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='elena.radu@usv.ro')), 2, 'BOTH');

-- Prof 5: Gheorghe Stan (Electronics)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-107'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='gheorghe.stan@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-203'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='gheorghe.stan@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-207'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='gheorghe.stan@usv.ro')), 2, 'BOTH');

-- Prof 6: Ana Stoica (OS/Networks)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-205'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ana.stoica@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-301'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ana.stoica@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-304'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='ana.stoica@usv.ro')), 2, 'BOTH');

-- Prof 7: Constantin Gheorghiu (UI/Web)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-208'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='constantin.gheorghiu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-307'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='constantin.gheorghiu@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-308'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='constantin.gheorghiu@usv.ro')), 2, 'BOTH');

-- Prof 8: Mihai Matei (AI/Advanced)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-305'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='mihai.matei@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-403'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='mihai.matei@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-407'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='mihai.matei@usv.ro')), 2, 'BOTH');

-- Prof 9: Florin Nistor (Software Eng/Distributed)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-401'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='florin.nistor@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-402'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='florin.nistor@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-408'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='florin.nistor@usv.ro')), 2, 'BOTH');

-- Prof 10: Roxana Dobre (Security/Mobile/IoT)
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-404'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='roxana.dobre@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-405'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='roxana.dobre@usv.ro')), 2, 'BOTH');
INSERT INTO course_offerings (course_id, group_id, teacher_id, weekly_hours, parity) VALUES ((SELECT id FROM courses WHERE code='C-406'),(SELECT id FROM groups WHERE code LIKE 'C-%' ORDER BY id LIMIT 1),(SELECT id FROM teacher_profiles WHERE user_id=(SELECT id FROM users WHERE email='roxana.dobre@usv.ro')), 2, 'BOTH');
