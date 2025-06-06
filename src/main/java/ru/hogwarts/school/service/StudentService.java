package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {

    public Student findStudent(long id);

    public void deleteStudents(long id);

    public Student editStudent(long id, Student student);

    public Collection<Student> getAllStudents();

    public Student createStudent(Student student);

    public Collection<Student> findByAgeBetween(int min, int max);

    public Faculty getFacultyByStudent(Long studentId);

    public Long getStudentCount();

    public Long getStudentAvgAge();

    public List<Student> getFiveLastStudent();

    public List<String> getNameStartingWithA();

    public Double getAvgAgeStream();
}
