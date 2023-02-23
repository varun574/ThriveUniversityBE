package com.example.springboot.thriveuniversitybackend.student.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentDto {
    private String id;
    @Pattern(regexp = "^\\d{2}b81a\\d{2}(\\w\\d|\\d{2})@thrive.ac.in$", message = "Must be well formed mail address.")
    private String email;
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number is must be in specified format.")
    private String rollNo;
}
