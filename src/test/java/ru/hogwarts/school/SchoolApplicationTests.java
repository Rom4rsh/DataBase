package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty testFaculty;
    private Student testStudent1;
    private Student testStudent2;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("TestFaculty");
        faculty.setColor("Red");
        return restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
    }

    private Student createTestStudent(Faculty faculty, String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        return restTemplate.postForObject(getUrl("/student"), student, Student.class);
    }

    @BeforeEach
    void setUp() {
        testFaculty = createTestFaculty();
        testStudent1 = createTestStudent(testFaculty, "TestStudent", 20);
        testStudent2 = createTestStudent(testFaculty, "AAA", 0);
    }

    @AfterEach
    void tearDown() {
        restTemplate.delete(getUrl("/student/" + testStudent1.getId()));
        restTemplate.delete(getUrl("/student/" + testStudent2.getId()));
        restTemplate.delete(getUrl("/faculty/" + testFaculty.getId()));
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void testCreateAndGetStudent() {
        ResponseEntity<Student> response = restTemplate.getForEntity(getUrl("/student/" + testStudent1.getId()), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("TestStudent");
    }

    @Test
    void testEditStudent() {
        testStudent1.setName("UpdatedName");
        HttpEntity<Student> request = new HttpEntity<>(testStudent1);
        ResponseEntity<Student> response = restTemplate.exchange(
                getUrl("/student/" + testStudent1.getId()),
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("UpdatedName");
    }

    @Test
    void testDeleteStudent() {
        restTemplate.delete(getUrl("/student/" + testStudent1.getId()));

        ResponseEntity<Student> response = restTemplate.getForEntity(getUrl("/student/" + testStudent1.getId()), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.exchange(
                getUrl("/student"),
                HttpMethod.GET,
                null,
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student[] students = response.getBody();
        assertThat(students).isNotNull();
        assertThat(students.length).isGreaterThanOrEqualTo(2);

        List<String> names = Arrays.stream(students).map(Student::getName).toList();
        assertThat(names).contains("TestStudent", "AAA");
    }

    @Test
    void testFindByAgeBetween() {
        String url = String.format("/student/findByAgeBetween?min=%d&max=%d", 18, 25);
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getUrl(url), Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void testGetFacultyByStudent() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getUrl("/student/" + testStudent1.getId() + "/faculty"), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("TestFaculty");
    }
}


//
//    @Test
//    void testCreateAndGetStudent() {
//        Faculty faculty = new Faculty();
//        faculty.setName("TEST");
//        faculty.setColor("TEST");
//
//        Faculty createdFaculty = restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
//        Assertions.assertThat(createdFaculty).isNotNull();
//        Assertions.assertThat(createdFaculty.getName()).isNotNull();
//        Assertions.assertThat(createdFaculty.getId()).isNotNull();
//
//        Student student = new Student();
//        student.setName("Test");
//        student.setAge(18);
//        student.setFaculty(createdFaculty);
//
//        Student createdStudent = restTemplate.postForObject(getUrl("/student"), student, Student.class);
//        Assertions.assertThat(createdStudent.getAge()).isEqualTo(18);
//        Assertions.assertThat(createdStudent.getId()).isNotNull();
//
//        ResponseEntity<Student> getStudent = restTemplate.getForEntity(getUrl("/student" + student.getId()), Student.class);
//        Assertions.assertThat(getStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        restTemplate.delete(getUrl("/student/" + createdStudent.getId()), createdStudent);
//        restTemplate.delete(getUrl("/faculty/" + createdFaculty.getId()), createdFaculty);
//    }
//
//    @Test
//    void testDeleteStudent() {
//
//        Faculty faculty = new Faculty();
//        faculty.setName("TEST");
//        faculty.setColor("TEST");
//        Faculty createdFaculty = restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
//
//        Student student = new Student();
//        student.setName("Test");
//        student.setAge(18);
//        student.setFaculty(createdFaculty);
//        Student createdStudent = restTemplate.postForObject(getUrl("/student"), student, Student.class);
//
//        ResponseEntity<Void> deleteResponse = restTemplate.exchange(getUrl("/student/" + createdStudent.getId()), HttpMethod.DELETE, null, Void.class);
//        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        ResponseEntity<Student> getResponse = restTemplate.getForEntity(getUrl("/student/" + createdStudent.getId()), Student.class);
//        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//        restTemplate.delete(getUrl("/faculty/" + createdFaculty.getId()), Faculty.class);
//    }
//
//    @Test
//    void testEditStudent() {
//        Faculty faculty = new Faculty();
//        faculty.setName("TEST");
//        faculty.setColor("TEST");
//        Faculty createdFaculty = restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
//
//        Student student = new Student();
//        student.setName("Test");
//        student.setAge(18);
//        student.setFaculty(createdFaculty);
//        Student createdStudent = restTemplate.postForObject(getUrl("/student"), student, Student.class);
//
//        createdStudent.setName("AAA");
//        createdStudent.setAge(0);
//
//        restTemplate.put(getUrl("/student/" + createdStudent.getId()), createdStudent);
//
//        Assertions.assertThat(createdStudent).isNotNull();
//        Assertions.assertThat(createdStudent.getName()).isEqualTo("AAA");
//        Assertions.assertThat(createdStudent.getAge()).isEqualTo(0);
//
//        restTemplate.delete(getUrl("/student/" + createdStudent.getId()), createdStudent);
//        restTemplate.delete(getUrl("/faculty/" + createdFaculty.getId()), createdFaculty);
//    }
//
//    @Test
//    void testGetAllStudents() {
//        Faculty faculty = new Faculty();
//        faculty.setName("TEST");
//        faculty.setColor("TEST");
//        Faculty createdFaculty = restTemplate.postForObject(getUrl("/faculty"), faculty, Faculty.class);
//
//        Student student = new Student();
//        student.setName("Test");
//        student.setAge(18);
//        student.setFaculty(createdFaculty);
//        Student createdStudent = restTemplate.postForObject(getUrl("/student"), student, Student.class);
//
//        ResponseEntity<Student[]> response = restTemplate.getForEntity(getUrl("/student"), Student[].class);
//        Student[] students = response.getBody();
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(students).isNotNull();
//        assertThat(students.length).isGreaterThan(0);
//
//        restTemplate.delete(getUrl("/student/" + createdStudent.getId()), createdStudent);
//        restTemplate.delete(getUrl("/faculty/" + createdFaculty.getId()), createdFaculty);
//    }
//}



