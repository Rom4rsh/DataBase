--liquibase formatted sql

--changeset romarsh:1
CREATE INDEX student_name_index ON student(name);

--changeset romarsh:2
CREATE INDEX faculty_name_index ON faculty(name);

