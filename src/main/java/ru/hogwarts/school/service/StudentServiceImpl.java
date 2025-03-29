package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
       return studentRepository.save(student);
    }

//    @Override
//    public Collection<Student> findByAge(int age) {
//        ArrayList<Student> result = new ArrayList<>();
//        for(Student student: students.values()){
//            if(student.getAge()==age){
//                result.add(student);
//            }
//        }
//        return result;
//    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public void deleteStudents(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student editStudent(long id, Student student) {
       return studentRepository.save(student);
    }

//    @Override
//    public Collection<Student> getAllStudents() {
//        return students.values();
//    }
}
