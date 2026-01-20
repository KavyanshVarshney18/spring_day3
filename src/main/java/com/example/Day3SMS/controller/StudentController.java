package com.example.Day3SMS.controller;

import com.example.Day3SMS.model.StudentModel;
import com.example.Day3SMS.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
   private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // Create API
    @PostMapping("/add-student")
    public StudentModel addStudent(@RequestBody StudentModel student){
        return service.addStudent(student);
    }
}
