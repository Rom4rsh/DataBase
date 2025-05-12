package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private int count = 0;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method create student");
        return studentRepository.save(student);
    }

    @Override
    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.debug("Was invoked method find by age min = {} between max = {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyByStudent(Long studentId) {
        logger.debug("Was invoked method get faculty by student id = {}", studentId);
        return studentRepository.findFacultyByStudentId(studentId);
    }

    @Override
    public Student findStudent(long id) {
        logger.warn("Was invoked method find student by id = {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteStudents(long id) {
        logger.info("Was invoked method find student by id = {}", id);
        studentRepository.deleteById(id);
    }

    @Override
    public Student editStudent(long id, Student student) {
        logger.info("Was invoked method edit student with id = {}", id);
        student.setId(id);
        return studentRepository.save(student);
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.debug("Was invoked method get all student");
        return studentRepository.findAll();
    }

    @Override
    public Long getStudentCount() {
        logger.debug("Was invoked method get student count");
        return studentRepository.getStudentCount();
    }

    @Override
    public Long getStudentAvgAge() {
        logger.debug("Was invoked method get student avg age ");
        return studentRepository.getStudentAvgAge();
    }

    @Override
    public List<Student> getFiveLastStudent() {
        logger.debug("Was invoked method get five last student ");
        return studentRepository.getFiveLastStudent();
    }

    @Override
    public List<String> getNameStartingWithA() {
        return studentRepository.findAll().stream().map(Student::getName).filter(Objects::nonNull).filter(name -> name.toUpperCase().startsWith("A")).map(String::toUpperCase).sorted().toList();
    }

    @Override
    public Double getAvgAgeStream() {
        return studentRepository.findAll().stream().filter(s -> s.getAge() != null).mapToDouble(Student::getAge).average().orElse(0.0);
    }

    @Override
    public void getParallelStudent() {
        List<String> names = studentRepository.findAll().stream().map(Student::getName).limit(6).collect(Collectors.toList());

        System.out.println("Operation 1 " + names.get(0));
        System.out.println("Operation 2 " + names.get(1));

        new Thread(() -> {
            System.out.println("Operation 3 " + names.get(2));
            System.out.println("Operation 4 " + names.get(3));
        }).start();

        new Thread(() -> {
            System.out.println("Operation 5 " + names.get(4));
            System.out.println("Operation 6 " + names.get(5));
        }).start();
    }

    private synchronized void printStudentName(String name, int number) {
        System.out.println("Operation " + number + " Name " + name + " Count " + count);
        count++;
    }

    @Override
    public void getSynchronizedStudentNames() {
        List<String> name = studentRepository.findAll().stream()
                .map(Student::getName)
                .limit(6)
                .collect(Collectors.toList());

        printStudentName(name.get(0), 1);
        printStudentName(name.get(1), 2);

        new Thread(() -> {
            printStudentName(name.get(2), 3);
            printStudentName(name.get(3), 4);
        }).start();

        new Thread(() -> {
            printStudentName(name.get(4), 5);
            printStudentName(name.get(5), 6);
        }).start();


    }
}

