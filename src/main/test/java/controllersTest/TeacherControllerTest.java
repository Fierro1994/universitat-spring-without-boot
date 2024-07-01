package controllersTest;

import org.example.controllers.TeacherController;
import org.example.dto.TeacherDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.TeacherService;
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

class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void testGetTeacher() throws Exception {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setName("Пётр");
        teacherDto.setEmail("1s@example.com");

        when(teacherService.getTeacher(anyLong())).thenReturn(teacherDto);

        mockMvc.perform(get("/teachers/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\"}"));
    }

    @Test
    void testGetTeacherNotFound() throws Exception {
        when(teacherService.getTeacher(anyLong())).thenThrow(new EntityNotFoundException("Преподаватель не найден"));

        mockMvc.perform(get("/teachers/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllTeachers() throws Exception {
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setName("Пётр");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setName("Алексей");

        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(teacherDto1, teacherDto2));

        mockMvc.perform(get("/teachers/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Пётр\"},{\"id\":2,\"name\":\"Алексей\"}]"));
    }

    @Test
    void testAddTeacher() throws Exception {
        TeacherDto savedTeacherDto = new TeacherDto();
        savedTeacherDto.setId(1L);
        savedTeacherDto.setName("Пётр");
        savedTeacherDto.setEmail("1s@example.com");

        when(teacherService.addTeacher(any(TeacherDto.class))).thenReturn(savedTeacherDto);

        mockMvc.perform(post("/teachers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Пётр\",\"email\":\"1s@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\"}"));
    }

    @Test
    void testUpdateTeacher() throws Exception {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setName("Пётр");
        teacherDto.setEmail("1s@example.com");

        when(teacherService.updateTeacher(any(TeacherDto.class))).thenReturn(teacherDto);

        mockMvc.perform(put("/teachers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Пётр\",\"email\":\"1s@example.com\"}"));
    }

    @Test
    void testUpdateTeacherNotFound() throws Exception {
        when(teacherService.updateTeacher(any(TeacherDto.class))).thenThrow(new RuntimeException("Преподаватель не найден"));

        mockMvc.perform(put("/teachers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Сергей\",\"email\":\"sergey@example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveTeacher() throws Exception {
        doNothing().when(teacherService).removeTeacher(anyLong());

        mockMvc.perform(delete("/teachers/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}