package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

@Service
public class FacultyServiceImpl implements FacultyService {

    @Autowired
   private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public void deleteFaculties(long id) {
         facultyRepository.deleteById(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
       return facultyRepository.save(faculty);
    }

//    public Collection<Faculty> findByColor(String color){
//        ArrayList<Faculty> result = new ArrayList<>();
//        for(Faculty faculty:faculties.values()){
//            if(Objects.equals(faculty.getColor(),color)){
//                result.add(faculty);
//            }
//        }
//        return result;
//    }
}
