package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyByStudent(Long studentId) {
        return studentRepository.findFacultyByStudentId(studentId);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteStudents(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student editStudent(long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

}
