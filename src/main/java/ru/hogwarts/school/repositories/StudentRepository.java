package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public Collection<Student> findByAgeBetween(int min, int max);

    @Query("SELECT s.faculty FROM Student s WHERE s.id = :studentId")
    public Faculty findFacultyByStudentId(@Param("studentId") Long studentId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Long getStudentCount();

    @Query(value = "SELECT AVG(age) from student", nativeQuery = true)
    Long getStudentAvgAge();

    @Query(value = "SElECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getFiveLastStudent();


}