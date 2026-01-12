-- Create secretary_profiles table
CREATE TABLE secretary_profiles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

-- Add department_id to courses table
-- SQLite doesn't support adding a foreign key column with NOT NULL in one command if table has data.
-- We will add it as nullable first.
ALTER TABLE courses ADD COLUMN department_id INTEGER REFERENCES departments(id) DEFAULT 1;

-- Optional: If you want to enforce NOT NULL later, you'd need to recreate the table in SQLite
-- For now, we leave it nullable to avoid complex migration logic with existing data.
-- However, we should try to update existing courses to a default department if possible, or leave null.
