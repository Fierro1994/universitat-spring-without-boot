package org.example.controllers;


import org.example.dto.StudentDto;
import org.example.dto.TeacherDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.models.Teacher;
import org.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teacherService.getTeacher(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping("/add")
    public ResponseEntity<TeacherDto> addTeacher(@RequestBody TeacherDto teacherDto) {
        return new ResponseEntity<>(teacherService.addTeacher(teacherDto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<TeacherDto> updateTeacher(@RequestBody TeacherDto teacherDto) {
        try {
            return new ResponseEntity<>(teacherService.updateTeacher(teacherDto), HttpStatus.OK);
        } catch (RuntimeException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTeacher(@PathVariable Long id) throws EntityNotFoundException {
        teacherService.removeTeacher(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}