package org.example.dto;
import java.util.HashSet;
import java.util.Set;

public class CourseDto {
    private Long id;
    private String name;
    private Set<StudentDto> students = new HashSet<>();
    private TeacherDto teacher = new TeacherDto();

    public CourseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeacherDto getTeacherDto() {
        return teacher;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacher = teacherDto;
    }

}
