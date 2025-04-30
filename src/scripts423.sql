--STEP 3

SELECT student.name,student.age,faculty.name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT SELECT student.name,student.age
FROM student
INNER JOIN avatar ON student.id = avatar.student_id;


<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>