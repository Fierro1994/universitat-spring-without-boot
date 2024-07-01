package org.example.service;

import org.example.repository.TeacherRepository;
import org.example.dto.TeacherDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.TeacherMapper;
import org.example.models.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public TeacherDto getTeacher(Long id) throws EntityNotFoundException {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()){
            return teacherMapper.toDto(teacher.get());
        } else {
            throw new EntityNotFoundException("Teacher not found");
        }
    }

    public List<TeacherDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teachers.forEach(teacher -> teacherDtoList.add(teacherMapper.toDto(teacher)));
        return teacherDtoList;
    }

    @Transactional
    public TeacherDto addTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.fromDto(teacherDto);
        teacherRepository.save(teacher);
        return teacherMapper.toDto(teacher);
    }

    @Transactional
    public TeacherDto updateTeacher(TeacherDto teacherDto) throws EntityNotFoundException {
        Teacher teacher = teacherMapper.fromDto(teacherDto);

        if (teacherRepository.existsById(teacher.getId())) {
            teacherRepository.save(teacher);
            return teacherMapper.toDto(teacher);
        } else {
            throw new EntityNotFoundException("Teacher not found");
        }
    }

    @Transactional
    public void removeTeacher(Long id) throws EntityNotFoundException {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Teacher not found");
        }
    }
}