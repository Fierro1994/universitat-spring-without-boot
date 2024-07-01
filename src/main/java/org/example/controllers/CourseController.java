package org.example.controllers;

import org.example.dto.CourseDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.getCourse(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
        return new ResponseEntity<>(courseService.addCourse(courseDto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody CourseDto courseDto) {
        try {

            return new ResponseEntity<>(courseService.updateCourse(courseDto), HttpStatus.OK);
        } catch (RuntimeException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCourse(@PathVariable Long id) throws EntityNotFoundException {
        courseService.removeCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}