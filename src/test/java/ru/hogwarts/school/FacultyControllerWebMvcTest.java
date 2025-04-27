package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndGetFaculty() throws Exception {
        Faculty faculty = new Faculty("AA", "white");
        faculty.setId(1L);

        when(facultyService.findFaculty(anyLong())).thenReturn(faculty);
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AA"))
                .andExpect(jsonPath("$.id").value(1L));

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AA"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.color").value("white"));
    }

    @Test
    void testDeleteFacultyById() throws Exception {
        mockMvc.perform(delete("/faculty/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditFacultyById() throws Exception {
        Faculty updateFaculty = new Faculty("VV", "Blue");
        updateFaculty.setId(3L);

        when(facultyService.editFaculty(eq(3L), any(Faculty.class))).thenReturn(updateFaculty);

        mockMvc.perform(put("/faculty/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFaculty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("VV"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void testGetAllFaculty() throws Exception {
        Faculty faculty4 = new Faculty("SS", "green");
        faculty4.setId(4L);
        Faculty faculty5 = new Faculty("GG", "red");
        faculty5.setId(5L);

        when(facultyService.getAllFaculty()).thenReturn(List.of(faculty4, faculty5));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("SS"))
                .andExpect(jsonPath("$[1].name").value("GG"))
                .andExpect(jsonPath("$[0].id").value(4L))
                .andExpect(jsonPath("$[1].id").value(5L))
                .andExpect(jsonPath("$[0].color").value("green"))
                .andExpect(jsonPath("$[1].color").value("red"));
    }

    @Test
    void testGetStudentsByFaculty() throws Exception {
        Faculty faculty6 = new Faculty("SS", "green");
        faculty6.setId(6L);
        Student student = new Student("Sasha",14);
        student.setId(2L);
        student.setFaculty(faculty6);

        List<Student> students = new ArrayList<>();
        students.add(student);

        when(facultyService.getStudentsByFaculty(6L)).thenReturn(students);

        mockMvc.perform(get("/faculty/6/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sasha"))
                .andExpect(jsonPath("$[0].id").value(2L));

    }

}
