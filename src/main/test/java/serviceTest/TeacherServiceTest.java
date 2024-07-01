package serviceTest;

import org.example.dto.TeacherDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.TeacherMapper;
import org.example.mappers.TeacherMapperImpl;
import org.example.models.Teacher;
import org.example.repository.TeacherRepository;
import org.example.service.TeacherService;
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
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Spy
    private TeacherMapper teacherMapper = new TeacherMapperImpl();

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Test Teacher");
        teacher.setEmail("test@teacher.com");

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setName("Test Teacher");
        teacherDto.setEmail("test@teacher.com");
    }

    @Test
    void testGetTeacher() throws EntityNotFoundException {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        TeacherDto result = teacherService.getTeacher(1L);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getName(), result.getName());
        assertEquals(teacherDto.getEmail(), result.getEmail());

        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTeacherNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teacherService.getTeacher(1L));
        assertEquals("Teacher not found", exception.getMessage());

        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllTeachers() {
        List<Teacher> teachers = Arrays.asList(teacher);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<TeacherDto> result = teacherService.getAllTeachers();
        assertEquals(1, result.size());
        assertEquals(teacherDto.getId(), result.get(0).getId());
        assertEquals(teacherDto.getName(), result.get(0).getName());
        assertEquals(teacherDto.getEmail(), result.get(0).getEmail());

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testAddTeacher() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        TeacherDto result = teacherService.addTeacher(teacherDto);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getName(), result.getName());
        assertEquals(teacherDto.getEmail(), result.getEmail());

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher() throws EntityNotFoundException {
        when(teacherRepository.existsById(1L)).thenReturn(true);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        TeacherDto result = teacherService.updateTeacher(teacherDto);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getName(), result.getName());
        assertEquals(teacherDto.getEmail(), result.getEmail());

        verify(teacherRepository, times(1)).existsById(1L);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacherNotFound() {
        when(teacherRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teacherService.updateTeacher(teacherDto));
        assertEquals("Teacher not found", exception.getMessage());

        verify(teacherRepository, times(1)).existsById(1L);
    }

    @Test
    void testRemoveTeacher() throws EntityNotFoundException {
        when(teacherRepository.existsById(1L)).thenReturn(true);

        teacherService.removeTeacher(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveTeacherNotFound() {
        when(teacherRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teacherService.removeTeacher(1L));
        assertEquals("Teacher not found", exception.getMessage());

        verify(teacherRepository, times(1)).existsById(1L);
    }
}