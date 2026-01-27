package com.example.Day3SMS.controller;

import com.example.Day3SMS.model.StudentModel;
import com.example.Day3SMS.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/students")
    public List<StudentModel> getStudents(){
        return service.students();
    }

    @PutMapping("/students/{id}")
    public StudentModel updateStudent(
            @PathVariable String id,
            @RequestBody StudentModel student
    ) {
        return service.updateStudent(id, student);
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable String id) {
        service.deleteStudent(id);
        return "Student deleted successfully";
    }




}
