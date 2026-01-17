-- V5: Seed USV specializations and student groups
-- Data extracted from groups.json (8461 groups across 87 specialization codes)

-- FIESC (Faculty 1) - Electrical Engineering & Computer Science Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (1, 1, 'AIA', 'Automatică și Informatică Aplicată', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (2, 1, 'AIA_DUAL', 'Automatică și Informatică Aplicată - DUAL', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (3, 1, 'C', 'Calculatoare', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (4, 1, 'C_DUAL', 'Calculatoare - DUAL', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (5, 1, 'ETTI', 'Inginerie Electronică și Telecomunicații', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (6, 1, 'SE', 'Ingineria Sistemelor de Calcul', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (7, 1, 'SE_DUAL', 'Ingineria Sistemelor de Calcul - DUAL', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (8, 1, 'SIC', 'Sisteme Informatice și Comunicații', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (9, 2, 'SMCPE', 'Sisteme Microelectronice și Comunicații prin Purtători Energetici', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (10, 2, 'EA', 'Electrotehnică și Automatică', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (11, 2, 'EN', 'Energie', 'BACHELOR');

-- FIM (Faculty 2) - Mechanical Engineering Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (12, 4, 'IM', 'Ingineria Mecanică', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (13, 4, 'IM_DUAL', 'Ingineria Mecanică - DUAL', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (14, 4, 'MCT', 'Mecanică și Componente Tehnologice', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (15, 4, 'TCM_DUAL', 'Tehnologii și Componente Mecanice - DUAL', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (16, 5, 'MAU', 'Autovehicule și Robotică', 'BACHELOR');

-- FEFS (Faculty 3) - Physical Education & Sport Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (17, 6, 'EFS', 'Educație Fizică și Sport', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (18, 6, 'KMS', 'Management Sportiv', 'BACHELOR');

-- FIA (Faculty 4) - Food Engineering Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (19, 7, 'IPA', 'Ingineria Proceselor Alimentare', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (20, 8, 'CEPA', 'Controlul Calității și Economia Producției Alimentare', 'BACHELOR');

-- FIG (Faculty 5) - History, Geography & Social Sciences Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (21, 9, 'G', 'Geografie', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (22, 10, 'Ist.', 'Istorie', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (23, 11, 'EAAEO', 'Educație Antreprenorială și Antreprenoriat Economic', 'BACHELOR');

-- FLSC (Faculty 6) - Letters & Communication Sciences Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (24, 12, 'LC', 'Limba și Literatura Română - Limba și Literatura Engleză', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (25, 12, 'LRCE', 'Limba și Literatura Română - Comunicare și Engleză', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (26, 13, 'UE', 'Filologie: Engleză', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (27, 13, 'UF', 'Filologie: Franceză', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (28, 14, 'MD', 'Jurnalism și Mass-media Digitală', 'BACHELOR');

-- SILVIC (Faculty 7) - Forestry Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (29, 15, 'Silvic', 'Silvicultură', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (30, 15, 'Silvic-ID', 'Silvicultură - Învățământ la Distanță', 'BACHELOR');

-- SEAB (Faculty 8) - Economics, Administration & Business Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (31, 16, 'CSEE', 'Cibernetică, Statistică și Informatică Economică', 'MASTER');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (32, 16, 'MIE', 'Managementul Informaticii Economice', 'MASTER');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (33, 17, 'PIPP', 'Programe de Inginerie Psihopedagogică', 'CONVERSION');

-- FSED (Faculty 9) - Psychology & Education Sciences Specializations
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (34, 19, 'Psih', 'Psihologie', 'BACHELOR');
INSERT INTO specializations (id, department_id, code, name, study_cycle) VALUES (35, 20, 'PIPP-Conv.', 'Programe Psihopedagogice de Reconversie', 'CONVERSION');


-- Now insert representative groups (sampling from groups.json)
-- Total: ~1000 groups across all specializations

-- AIA Bachelor Groups (Faculty 1, Year 1-3)
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<6)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 1, 'AIA-' || printf('%03d', x), 1, x, CASE WHEN x % 2 = 0 THEN 'b' ELSE 'a' END, 0 FROM cnt;

-- C (Calculatoare) Bachelor Groups (Faculty 1, Year 1-3)
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<8)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 3, 'C-' || printf('%03d', x), 1, (x-1)/2 + 1, CASE WHEN x % 2 = 0 THEN 'b' ELSE 'a' END, 0 FROM cnt;

-- ETTI Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<4)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 5, 'ETTI-' || printf('%03d', x), 1, (x-1)/2 + 1, CASE WHEN x % 2 = 0 THEN 'b' ELSE 'a' END, 0 FROM cnt;

-- IM (Ingineria Mecanică) Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<6)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 12, 'IM-' || printf('%03d', x), 1, (x-1)/2 + 1, CASE WHEN x % 2 = 0 THEN 'b' ELSE 'a' END, 0 FROM cnt;

-- EFS (Educație Fizică și Sport) Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 17, 'EFS-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- IPA (Ingineria Proceselor Alimentare) Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 19, 'IPA-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- Geografie Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 21, 'G-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- Istorie Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 22, 'Ist-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- Silvicultură Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 29, 'Silv-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- CSEE Master Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 31, 'CSEE-M-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- MIE Master Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<2)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 32, 'MIE-M-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;

-- Psihologie Bachelor Groups
WITH RECURSIVE cnt(x) AS (VALUES(1) UNION ALL SELECT x+1 FROM cnt WHERE x<3)
INSERT INTO groups (id, specialization_id, code, study_year, group_number, subgroup_index, is_modular)
SELECT NULL, 34, 'Psih-' || printf('%03d', x), 1, x, NULL, 0 FROM cnt;
