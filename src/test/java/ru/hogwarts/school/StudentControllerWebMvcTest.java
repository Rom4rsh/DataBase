package ru.hogwarts.school;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockitoSpyBean
//    private StudentRepository studentRepository;
//
//    @InjectMocks
//    private StudentController studentController;

    @Test
    void testGetAndCreateStudent() throws Exception {
        Student student = new Student("Harry", 12);
        student.setId(1L);

        when(studentService.findStudent(1L)).thenReturn(student);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry"));

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void testDeleteStudentsById() throws Exception {
        mockMvc.perform(delete("/student/2"))
                .andExpect(status().isOk());
    }

    @Test
    void editStudentById() throws Exception {
        Student updateStudent = new Student("Tony", 13);
        updateStudent.setId(3L);

        when(studentService.editStudent(eq(3L), any(Student.class)))
                .thenReturn(updateStudent);


        mockMvc.perform(put("/student/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStudent))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tony"))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.age").value(13));
    }

    @Test
    void getAllStudentsTest() throws Exception {
        Student student4 = new Student("Harry", 12);
        student4.setId(4L);
        Student student5 = new Student("Katya", 25);
        student5.setId(5L);

        when(studentService.getAllStudents()).thenReturn(List.of(student4, student5));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[1].name").value("Katya"));
    }

    @Test
    void findStudentByAgeBetween() throws Exception {
        Student student7 = new Student("Katya", 25);
        student7.setId(7L);
        Student student8 = new Student("Sasha", 33);
        student8.setId(8L);
        Student student9 = new Student("Masha", 19);
        student9.setId(9L);

        when(studentService.findByAgeBetween(20, 40)).thenReturn(List.of(student7, student8));

        mockMvc.perform(get("/student//findByAgeBetween")
                        .param("min", "20")
                        .param("max", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Katya"))
                .andExpect(jsonPath("$[1].name").value("Sasha"))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void getFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty("Faculty", "Red");
        faculty.setId(2L);
        Student student10 = new Student("Eva", 18);
        student10.setId(10L);
        student10.setFaculty(faculty);

        when(studentService.getFacultyByStudent(10L)).thenReturn(faculty);

        mockMvc.perform(get("/student/10/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Faculty"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

}



