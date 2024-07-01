package org.example.controllers;

import org.example.dto.CourseDto;
import org.example.dto.StudentDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.models.Student;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(studentService.getStudent(id), HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/add")
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto studentDto) {
        return new ResponseEntity<>(studentService.addStudent(studentDto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentDto studentDto) {
        try {
            return new ResponseEntity<>(studentService.updateStudent(studentDto), HttpStatus.OK);
        } catch (RuntimeException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) throws EntityNotFoundException {
        studentService.removeStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}