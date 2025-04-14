package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface FacultyService {

    public Faculty createFaculty(Faculty faculty);


    public Faculty findFaculty(long id);

    public void deleteFaculties(long id);

    public Faculty editFaculty(long id, Faculty faculty);

    public Collection<Faculty> getAllFaculty();

    public List<Student> getStudentsByFaculty(Long facultyId);

}
