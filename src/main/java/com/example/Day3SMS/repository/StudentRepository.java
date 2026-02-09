package com.example.Day3SMS.repository;

import com.example.Day3SMS.model.StudentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<StudentModel, String> {

    List<StudentModel> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}

