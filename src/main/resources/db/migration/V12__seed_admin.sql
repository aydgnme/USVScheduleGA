-- V12: Seed Admin Account
-- Ensures there is at least one admin user
INSERT INTO users (email, password, role, enabled) VALUES ('admin@usv.ro', '$2a$10$o40p7mzIZxjzNXbsNJFzjeUMsqPxAv5EZzzmaPwYz/LJ8/JJQ.JBy', 'ADMIN', 1);
