package serviceTest;

import org.example.dto.CourseDto;
import org.example.dto.StudentDto;
import org.example.dto.TeacherDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.CourseMapper;
import org.example.mappers.CourseMapperImpl;
import org.example.models.Course;
import org.example.models.Teacher;
import org.example.repository.CourseRepository;
import org.example.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Spy
    private CourseMapper courseMapper = new CourseMapperImpl();

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseDto courseDto;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setName("Test Course");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Test Teacher");
        course.setTeacher(teacher);

        courseDto = new CourseDto();
        courseDto.setId(1L);
        courseDto.setName("Test Course");

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setName("Test Teacher");
        courseDto.setTeacherDto(teacherDto);
    }
    @Test
    void testGetCourse() throws EntityNotFoundException {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourse(1L);
        assertEquals(courseDto.getId(), result.getId());
        assertEquals(courseDto.getName(), result.getName());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> courseService.getCourse(1L));
        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = Arrays.asList(course);

        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDto> result = courseService.getAllCourses();
        assertEquals(1, result.size());
        assertEquals(courseDto.getId(), result.get(0).getId());
        assertEquals(courseDto.getName(), result.get(0).getName());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto result = courseService.addCourse(courseDto);
        assertEquals(courseDto.getId(), result.getId());
        assertEquals(courseDto.getName(), result.getName());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourse() throws EntityNotFoundException {
        when(courseRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto result = courseService.updateCourse(courseDto);
        assertEquals(courseDto.getId(), result.getId());
        assertEquals(courseDto.getName(), result.getName());

        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourseNotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> courseService.updateCourse(courseDto));
        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository, times(1)).existsById(1L);
    }

    @Test
    void testRemoveCourse() throws EntityNotFoundException {
        when(courseRepository.existsById(1L)).thenReturn(true);

        courseService.removeCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveCourseNotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> courseService.removeCourse(1L));
        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository, times(1)).existsById(1L);
    }

}