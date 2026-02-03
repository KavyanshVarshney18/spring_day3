package com.example.Day3SMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 90, message = "Age must be at most 90")
    private int age;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
