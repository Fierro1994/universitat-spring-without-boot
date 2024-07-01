package controllersTest;

import org.example.controllers.StudentController;
import org.example.dto.StudentDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)

class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testGetStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName("Пётр");
        studentDto.setEmail("1s@example.com");
        studentDto.setAge(20);

        when(studentService.getStudent(anyLong())).thenReturn(studentDto);

        mockMvc.perform(get("/students/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\",\"age\":20}"));
    }

    @Test
    void testGetStudentNotFound() throws Exception {
        when(studentService.getStudent(anyLong())).thenThrow(new EntityNotFoundException("Student not found"));

        mockMvc.perform(get("/students/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllStudents() throws Exception {
        StudentDto studentDto1 = new StudentDto();
        studentDto1.setId(1L);
        studentDto1.setName("Пётр");

        StudentDto studentDto2 = new StudentDto();
        studentDto2.setId(2L);
        studentDto2.setName("Иван");

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(studentDto1, studentDto2));

        mockMvc.perform(get("/students/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Пётр\"},{\"id\":2,\"name\":\"Иван\"}]"));
    }

    @Test
    void testAddStudent() throws Exception {
        StudentDto savedStudentDto = new StudentDto();
        savedStudentDto.setId(1L);
        savedStudentDto.setName("Пётр");
        savedStudentDto.setEmail("1s@example.com");
        savedStudentDto.setAge(20);

        when(studentService.addStudent(any(StudentDto.class))).thenReturn(savedStudentDto);

        mockMvc.perform(post("/students/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Пётр\",\"email\":\"1s@example.com\",\"age\":20}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\",\"age\":20}"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName("Пётр");
        studentDto.setEmail("1s@example.com");
        studentDto.setAge(21);

        when(studentService.updateStudent(any(StudentDto.class))).thenReturn(studentDto);

        mockMvc.perform(put("/students/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\",\"age\":21}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\",\"age\":21}"));
    }

    @Test
    void testUpdateStudentNotFound() throws Exception {
        when(studentService.updateStudent(any(StudentDto.class))).thenThrow(new RuntimeException("Student not found"));

        mockMvc.perform(put("/students/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\",\"age\":21}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveStudent() throws Exception {
        doNothing().when(studentService).removeStudent(anyLong());

        mockMvc.perform(delete("/students/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}