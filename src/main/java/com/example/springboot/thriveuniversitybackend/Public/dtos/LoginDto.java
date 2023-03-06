package com.example.springboot.thriveuniversitybackend.Public.dtos;

import com.example.springboot.thriveuniversitybackend.validators.annotations.Password;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {
    @Pattern(regexp = "^[a-z]+\\.[a-z]+@thrive.ac.in$", message = "Must be well formed mail address.")
    private String email;
    @Password
    private String password;
}
