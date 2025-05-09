package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;
import java.util.stream.Stream;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.warn("Was invoked method for find faculty by id = {}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public void deleteFaculties(long id) {
        logger.info("Was invoked method for delete faculty by id = {}", id);
        facultyRepository.deleteById(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        logger.info("Was invoked method for edit faculty with id = {}", id);
        Faculty updateFaculty = facultyRepository.save(faculty);
        logger.debug("Faculty updated = {}", updateFaculty);
        return updateFaculty;
    }

    public Collection<Faculty> getAllFaculty() {
        logger.debug("Was invoked method for get all faculty");
        return facultyRepository.findAll();
    }

    public List<Student> getStudentsByFaculty(Long facultyId) {
       logger.warn("Was invoked method for get students by faculty with id = {}", facultyId);
        return facultyRepository.findStudentsByFacultyId(facultyId);
    }

    @Override
    public String getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse("No faculty");
    }

    @Override
    public int getSum() {
        int sum;
        sum = Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        return sum;
    }
}
