package controllersTest;

import org.example.controllers.CourseController;
import org.example.dto.CourseDto;
import org.example.dto.StudentDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.CourseService;
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

class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetControllerCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(1L);
        courseDto.setName("java");

        when(courseService.getCourse(anyLong())).thenReturn(courseDto);

        mockMvc.perform(get("/courses/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"java\"}"));
    }

    @Test
    void testGetCourseNotFound() throws Exception {
        when(courseService.getCourse(anyLong())).thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(get("/courses/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCoursetNotFound() throws Exception {
        when(courseService.updateCourse(any(CourseDto.class))).thenThrow(new RuntimeException("Course not found"));

        mockMvc.perform(put("/courses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\",\"age\":21}"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetControllerAllCourses() throws Exception {
        CourseDto courseDto1 = new CourseDto();
        courseDto1.setId(1L);
        courseDto1.setName("java");

        CourseDto courseDto2 = new CourseDto();
        courseDto2.setId(2L);
        courseDto2.setName("js");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(courseDto1, courseDto2));

        mockMvc.perform(get("/courses/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"java\"},{\"id\":2,\"name\":\"js\"}]"));
    }

    @Test
    void testDoPost_AddCourse() throws Exception {


        CourseDto savedCourseDto = new CourseDto();
        savedCourseDto.setId(1L);
        savedCourseDto.setName("java");

        when(courseService.addCourse(any(CourseDto.class))).thenReturn(savedCourseDto);

        mockMvc.perform(post("/courses/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"java\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"java\"}"));
    }

    @Test
    void testDoPut_UpdateCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(1L);
        courseDto.setName("java");

        when(courseService.updateCourse(any(CourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(put("/courses/update", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"java\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"java\"}"));
    }

    @Test
    void testDoRemove_RemoveCourse() throws Exception {
        doNothing().when(courseService).removeCourse(anyLong());

        mockMvc.perform(delete("/courses/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}