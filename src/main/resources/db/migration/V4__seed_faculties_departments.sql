-- V4: Seed USV faculties and departments
-- Data extracted from groups.json and teacher.json

-- Insert Faculties
INSERT INTO faculties (id, code, name, short_name) VALUES (1, 'FIESC', 'Facultatea de Inginerie Electrică și Știința Calculatoarelor', 'FIESC');
INSERT INTO faculties (id, code, name, short_name) VALUES (2, 'FIM', 'Facultatea de Inginerie Mecanică, Autovehicule și Robotică', 'FIM');
INSERT INTO faculties (id, code, name, short_name) VALUES (3, 'FEFS', 'Facultatea de Educație Fizică și Sport', 'FEFS');
INSERT INTO faculties (id, code, name, short_name) VALUES (4, 'FIA', 'Facultatea de Inginerie Alimentară', 'FIA');
INSERT INTO faculties (id, code, name, short_name) VALUES (5, 'FIG', 'Facultatea de Istorie, Geografie și Științe Sociale', 'FIG');
INSERT INTO faculties (id, code, name, short_name) VALUES (6, 'FLSC', 'Facultatea de Litere și Științe ale Comunicării', 'FLSC');
INSERT INTO faculties (id, code, name, short_name) VALUES (7, 'SILVIC', 'Facultatea de Silvicultură', 'SILVIC');
INSERT INTO faculties (id, code, name, short_name) VALUES (8, 'SEAB', 'Facultatea de Economie, Administrație și Afaceri', 'SEAB');
INSERT INTO faculties (id, code, name, short_name) VALUES (9, 'FSED', 'Facultatea de Psihologie și Științe ale Educației', 'FSED');
INSERT INTO faculties (id, code, name, short_name) VALUES (10, 'FDSA', 'Facultatea de Drept și Științe Administrative', 'FDSA');
INSERT INTO faculties (id, code, name, short_name) VALUES (11, 'FMSB', 'Facultatea de Medicină și Științe Biologice', 'FMSB');

-- FIESC (Faculty 1) - Electrical Engineering & Computer Science
INSERT INTO departments (id, faculty_id, code, name) VALUES (1, 1, 'CALC', 'Calculatoare');
INSERT INTO departments (id, faculty_id, code, name) VALUES (2, 1, 'ELECTR', 'Electrotehnică');
INSERT INTO departments (id, faculty_id, code, name) VALUES (3, 1, 'AUTOMAT', 'Automatică și Electronică');

-- FIM (Faculty 2) - Mechanical Engineering
INSERT INTO departments (id, faculty_id, code, name) VALUES (4, 2, 'MECH', 'Mecanică și Tehnologii');
INSERT INTO departments (id, faculty_id, code, name) VALUES (5, 2, 'AUTO', 'Autovehicule și Robotică');

-- FEFS (Faculty 3) - Physical Education & Sport
INSERT INTO departments (id, faculty_id, code, name) VALUES (6, 3, 'EDFS', 'Educație Fizică și Sport');

-- FIA (Faculty 4) - Food Engineering
INSERT INTO departments (id, faculty_id, code, name) VALUES (7, 4, 'TECH_ALI', 'Tehnologii Alimentare');
INSERT INTO departments (id, faculty_id, code, name) VALUES (8, 4, 'BIOSEC', 'Biotehnologii și Siguranța Alimentară');

-- FIG (Faculty 5) - History, Geography & Social Sciences
INSERT INTO departments (id, faculty_id, code, name) VALUES (9, 5, 'GEOG', 'Geografie');
INSERT INTO departments (id, faculty_id, code, name) VALUES (10, 5, 'ISTORIE', 'Istorie');
INSERT INTO departments (id, faculty_id, code, name) VALUES (11, 5, 'STIINTE_SOC', 'Științe Sociale și Politice');

-- FLSC (Faculty 6) - Letters & Communication Sciences
INSERT INTO departments (id, faculty_id, code, name) VALUES (12, 6, 'LIMBA_RO', 'Limba și Literatura Română');
INSERT INTO departments (id, faculty_id, code, name) VALUES (13, 6, 'LIMBI_STR', 'Limbi și Literaturi Străine');
INSERT INTO departments (id, faculty_id, code, name) VALUES (14, 6, 'COMU', 'Științe ale Comunicării');

-- SILVIC (Faculty 7) - Forestry
INSERT INTO departments (id, faculty_id, code, name) VALUES (15, 7, 'SILV', 'Silvicultură și Protecția Mediului');

-- SEAB (Faculty 8) - Economics, Administration & Business
INSERT INTO departments (id, faculty_id, code, name) VALUES (16, 8, 'ECON', 'Economie și Informatică Economică');
INSERT INTO departments (id, faculty_id, code, name) VALUES (17, 8, 'ACCT', 'Contabilitate, Audit și Finanțe');
INSERT INTO departments (id, faculty_id, code, name) VALUES (18, 8, 'MNGMT', 'Management și Administrarea Afacerilor');

-- FSED (Faculty 9) - Psychology & Education Sciences
INSERT INTO departments (id, faculty_id, code, name) VALUES (19, 9, 'PSIH', 'Psihologie');
INSERT INTO departments (id, faculty_id, code, name) VALUES (20, 9, 'EDUC', 'Științe ale Educației');
INSERT INTO departments (id, faculty_id, code, name) VALUES (21, 9, 'PSIPED', 'Psihopedagogie');

-- FDSA (Faculty 10) - Law & Administrative Sciences
INSERT INTO departments (id, faculty_id, code, name) VALUES (22, 10, 'DREPT', 'Drept și Științe Administrative');

-- FMSB (Faculty 11) - Medicine & Biological Sciences
INSERT INTO departments (id, faculty_id, code, name) VALUES (23, 11, 'BIO', 'Științe Biologice');
INSERT INTO departments (id, faculty_id, code, name) VALUES (24, 11, 'MED', 'Medicină');
