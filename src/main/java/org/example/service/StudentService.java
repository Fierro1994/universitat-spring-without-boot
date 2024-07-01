package org.example.service;

import org.example.repository.StudentRepository;
import org.example.dto.StudentDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.StudentMapper;
import org.example.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;



    public StudentDto getStudent(Long id) throws EntityNotFoundException {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()){
            return studentMapper.toDto(student.get());
        } else {
            throw new EntityNotFoundException("Student not found");
        }
    }

    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtoList = new ArrayList<>();
        students.forEach(student -> studentDtoList.add(studentMapper.toDto(student)));
        return studentDtoList;
    }

    @Transactional
    public StudentDto addStudent(StudentDto studentDto) {
        Student student = studentMapper.fromDto(studentDto);
        studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto updateStudent(StudentDto studentDto) throws EntityNotFoundException {
        Student student = studentMapper.fromDto(studentDto);

        if (studentRepository.existsById(student.getId())) {
            studentRepository.save(student);
            return studentMapper.toDto(student);
        } else {
            throw new EntityNotFoundException("Student not found");
        }
    }

    @Transactional
    public void removeStudent(Long id) throws EntityNotFoundException {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Student not found");
        }
    }
}