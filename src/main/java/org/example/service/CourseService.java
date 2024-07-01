package org.example.service;

import org.example.repository.CourseRepository;
import org.example.dto.CourseDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.CourseMapper;
import org.example.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseMapper courseMapper;


    public CourseDto getCourse(Long id) throws EntityNotFoundException {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return courseMapper.toDto(course.get());
        } else {
            throw new EntityNotFoundException("Course not found");
        }
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtoList = new ArrayList<>();
        courses.forEach(course -> courseDtoList.add(courseMapper.toDto(course)));
        return courseDtoList;
    }

    @Transactional
    public CourseDto addCourse(CourseDto courseDto) {
        Course course = courseMapper.fromDto(courseDto);
        courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Transactional
    public CourseDto updateCourse(CourseDto courseDto) throws EntityNotFoundException {
        Course course = courseMapper.fromDto(courseDto);
        if (courseRepository.existsById(course.getId())) {
            courseRepository.save(course);
            return courseMapper.toDto(course);
        } else {
            throw new EntityNotFoundException("Course not found");
        }
    }

    @Transactional
    public void removeCourse(Long id) throws EntityNotFoundException {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Course not found");
        }
    }
}