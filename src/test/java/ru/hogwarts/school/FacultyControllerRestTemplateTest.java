package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @LocalServerPort
    private int port;

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        return restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
    }

    private Student createTestStudent(Faculty faculty) {
        Student student = new Student();
        student.setName("Harry");
        student.setAge(17);
        student.setFaculty(faculty);
        return restTemplate.postForObject(getUrl("/student"), student, Student.class);
    }

    @Test
    void testCreateAndGetFaculty() {
        Faculty createdFaculty = createTestFaculty();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getUrl("/faculty/" + createdFaculty.getId()), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");

        restTemplate.delete(getUrl("/faculty/" + createdFaculty.getId()));
    }

    @Test
    void testGetAllFaculties() {
        Faculty faculty = createTestFaculty();

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(getUrl("/faculty"), Faculty[].class);
        Faculty[] faculties = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties).isNotNull();
        assertThat(faculties.length).isGreaterThan(0);

        restTemplate.delete(getUrl("/faculty/" + faculty.getId()));
    }

    @Test
    void testEditFaculty() {
        Faculty faculty = createTestFaculty();
        faculty.setName("Updated");

        HttpEntity<Faculty> request = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(getUrl("/faculty/" + faculty.getId()), HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated");

        restTemplate.delete(getUrl("/faculty/" + faculty.getId()));
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = createTestFaculty();

        restTemplate.delete(getUrl("/faculty/" + faculty.getId()));

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getUrl("/faculty/" + faculty.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetStudentsByFaculty() {
        Faculty faculty = createTestFaculty();
        Student student = createTestStudent(faculty);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(getUrl("/faculty/" + faculty.getId() + "/students"), Student[].class);
        Student[] students = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(students).isNotNull();
        assertThat(students.length).isGreaterThan(0);
        assertThat(students[0].getName()).isEqualTo("Harry");

        restTemplate.delete(getUrl("/student/" + student.getId()));
        restTemplate.delete(getUrl("/faculty/" + faculty.getId()));
    }
}

