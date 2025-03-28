package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private static long count = 0;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++count);
        faculties.put(count, faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty deleteFaculties(long id) {
        return faculties.remove(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            return null;
        }
        faculties.put(id, faculty);
        return faculty;
    }

    public Collection<Faculty> findByColor(String color){
        ArrayList<Faculty> result = new ArrayList<>();
        for(Faculty faculty:faculties.values()){
            if(Objects.equals(faculty.getColor(),color)){
                result.add(faculty);
            }
        }
        return result;
    }
}
