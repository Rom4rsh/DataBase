package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {

    public Faculty createFaculty(Faculty faculty);


    public Faculty findFaculty(long id);

    public void deleteFaculties(long id);

    public Faculty editFaculty(long id, Faculty faculty);

//    public Collection<Faculty> findByColor(String color);


}
