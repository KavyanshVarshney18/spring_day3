package com.example.Day3SMS.dto;

public class StudentResponseDto {

    private String id;
    private String name;
    private int age;
    private String email;

    public StudentResponseDto(String id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
