package com.example.Day3SMS.controller;

import com.example.Day3SMS.dto.StudentRequestDto;
import com.example.Day3SMS.dto.StudentResponseDto;
import com.example.Day3SMS.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public StudentResponseDto addStudent(
            @Valid @RequestBody StudentRequestDto dto
    ) {
        return service.addStudent(dto);
    }

    @GetMapping
    public List<StudentResponseDto> getStudents() {
        return service.getAllStudents();
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentRequestDto dto
    ) {
        return service.updateStudent(id, dto);
    }


    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable String id) {
        service.deleteStudent(id);
        return "Student deleted successfully";
    }
}
