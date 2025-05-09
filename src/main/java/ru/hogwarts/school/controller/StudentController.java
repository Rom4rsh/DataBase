package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
    }


    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        if (student.getAge() == null) {
            student.setAge(20);
        }
        return studentService.createStudent(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> editStudent(@RequestBody Student student, @PathVariable Long id) {
        Student foundStudent = studentService.editStudent(id, student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        studentService.deleteStudents(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> allStudent = new ArrayList<>(studentService.getAllStudents());
        if (allStudent == null || allStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStudent);
    }

    @GetMapping("/findByAgeBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        Collection<Student> students = studentService.findByAgeBetween(min, max);
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudent(id);
        return faculty != null ? ResponseEntity.ok(faculty) : ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getStudentCount() {
        Long student = studentService.getStudentCount();
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();

    }

    @GetMapping("/avgAge")
    public ResponseEntity<Long> getStudentAvgAge() {
        return ResponseEntity.ok(studentService.getStudentAvgAge());
    }

    @GetMapping("/lastStudent")
    public ResponseEntity<List<Student>> getFiveLastStudent() {
        List<Student> student = studentService.getFiveLastStudent();
        return ResponseEntity.ok(student);
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getNameStartingWithA() {
        return studentService.getNameStartingWithA();
    }

    @GetMapping("/avg-age-stream")
    public Double getAvgAgeStream() {
        return studentService.getAvgAgeStream();
    }

}

