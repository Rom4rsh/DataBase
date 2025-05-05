--Step 1
ALTER TABLE student
ADD CONSTRAINT age_constraint CHECK (age > 16); --Возраст студента не может быть меньше 16

ALTER TABLE student
ALTER COLUMN name SET NOT NULL,
ADD CONSTRAINT unique_student_name UNIQUE (name);

ALTER TABLE faculty
ADD CONSTRAINT faculty_constraint UNIQUE (name,color);

ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;







