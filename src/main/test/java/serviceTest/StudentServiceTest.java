package serviceTest;

import org.example.dto.CourseDto;
import org.example.dto.StudentDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.StudentMapper;
import org.example.mappers.StudentMapperImpl;
import org.example.models.Course;
import org.example.models.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private StudentMapper studentMapper = new StudentMapperImpl();

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setEmail("test@student.com");
        student.setAge(20);

        Course course = new Course();
        course.setId(1L);
        course.setName("Test Course");
        student.setCourses(new HashSet<>(List.of(course)));

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName("Test Student");
        studentDto.setEmail("test@student.com");
        studentDto.setAge(20);

        CourseDto courseDto = new CourseDto();
        courseDto.setId(1L);
        courseDto.setName("Test Course");
        studentDto.setCourses(new HashSet<>(List.of(courseDto)));
    }

    @Test
    void testGetStudent() throws EntityNotFoundException {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDto result = studentService.getStudent(1L);
        assertEquals(studentDto.getId(), result.getId());
        assertEquals(studentDto.getName(), result.getName());
        assertEquals(studentDto.getEmail(), result.getEmail());
        assertEquals(studentDto.getAge(), result.getAge());
        assertEquals(1, result.getCourses().size());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.getStudent(1L));
        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Collections.singletonList(student);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> result = studentService.getAllStudents();
        assertEquals(1, result.size());
        assertEquals(studentDto.getId(), result.getFirst().getId());
        assertEquals(studentDto.getName(), result.getFirst().getName());
        assertEquals(studentDto.getEmail(), result.getFirst().getEmail());
        assertEquals(studentDto.getAge(), result.getFirst().getAge());
        assertEquals(1, result.getFirst().getCourses().size());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testAddStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto result = studentService.addStudent(studentDto);
        assertEquals(studentDto.getId(), result.getId());
        assertEquals(studentDto.getName(), result.getName());
        assertEquals(studentDto.getEmail(), result.getEmail());
        assertEquals(studentDto.getAge(), result.getAge());
        assertEquals(1, result.getCourses().size());

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent() throws EntityNotFoundException {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto result = studentService.updateStudent(studentDto);
        assertEquals(studentDto.getId(), result.getId());
        assertEquals(studentDto.getName(), result.getName());
        assertEquals(studentDto.getEmail(), result.getEmail());
        assertEquals(studentDto.getAge(), result.getAge());
        assertEquals(1, result.getCourses().size());

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudentNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.updateStudent(studentDto));
        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository, times(1)).existsById(1L);
    }

    @Test
    void testRemoveStudent() throws EntityNotFoundException {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.removeStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveStudentNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> studentService.removeStudent(1L));
        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository, times(1)).existsById(1L);
    }
}