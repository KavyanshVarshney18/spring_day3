package com.example.Day3SMS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "student")

public class StudentModel {
    @Id
    private String id;
    private String name;
    private int age;
    private String email;
}
