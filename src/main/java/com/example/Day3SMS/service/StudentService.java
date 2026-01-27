package com.example.Day3SMS.service;

import com.example.Day3SMS.model.StudentModel;
import com.example.Day3SMS.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    //Create
    public StudentModel addStudent(StudentModel student ) {
        System.out.println("STUDENT RECEIVED 👉 " + student);
        return repository.save(student);
    }

    //display student
    public List<StudentModel> students(){
        List<StudentModel> list = repository.findAll();
        System.out.println("STUDENTS FROM DB 👉 " + list);
        return list;
    }


    //update
    public StudentModel updateStudent(String id, StudentModel updatedStudent) {

        StudentModel existingStudent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setAge(updatedStudent.getAge());
        existingStudent.setEmail(updatedStudent.getEmail());

        return repository.save(existingStudent);
    }

    public void deleteStudent(String id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }

        repository.deleteById(id);
    }



}

