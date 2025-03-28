package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
    private final HashMap<Long, Student> students = new HashMap<>();
    private static long count = 0;

    @Override
    public Student createStudent(Student student) {
        student.setId(++count);
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Collection<Student> findByAge(int age) {
        ArrayList<Student> result = new ArrayList<>();
        for(Student student: students.values()){
            if(student.getAge()==age){
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public Student findStudent(long id) {
        return students.get(id);
    }

    @Override
    public Student deleteStudents(long id) {
        return students.remove(id);
    }

    @Override
    public Student editStudent(long id, Student student) {
        if (!students.containsKey(id)) {
            return null;
        }
        students.put(id, student);
        return student;
    }

    @Override
    public Collection<Student> getAllStudents() {
        return students.values();
    }
}
