package com.example.Day3SMS.service;

import com.example.Day3SMS.dto.StudentRequestDto;
import com.example.Day3SMS.dto.StudentResponseDto;
import com.example.Day3SMS.exception.StudentNotFoundException;
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

    public StudentResponseDto addStudent(StudentRequestDto dto) {

        StudentModel student = new StudentModel();
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());

        return map(repository.save(student));
    }

    public List<StudentResponseDto> getAllStudents() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<StudentResponseDto> searchStudents(String query) {
        if (query == null || query.isBlank()) {
            return getAllStudents();
        }
        return repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream()
                .map(this::map)
                .toList();
    }

    public StudentResponseDto updateStudent(String id, StudentRequestDto dto) {

        StudentModel student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with id: " + id)
                );

        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());

        return map(repository.save(student));
    }

    public void deleteStudent(String id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private StudentResponseDto map(StudentModel s) {
        return new StudentResponseDto(
                s.getId(),
                s.getName(),
                s.getAge(),
                s.getEmail()
        );
    }

    public StudentResponseDto patchStudent(String id, StudentRequestDto dto) {

        StudentModel student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update ONLY fields that are NOT null
        if (dto.getName() != null && !dto.getName().isBlank()) {
            student.setName(dto.getName());
        }

        if (dto.getAge() > 0) {
            student.setAge(dto.getAge());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            student.setEmail(dto.getEmail());
        }

        StudentModel updated = repository.save(student);
        return map(updated);
    }

}
