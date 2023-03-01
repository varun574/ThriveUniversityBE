package com.example.springboot.thriveuniversitybackend.Public;

import com.example.springboot.thriveuniversitybackend.validators.annotations.Password;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {
    @Pattern(regexp = "^\\d{2}b81a\\d{2}(\\w\\d|\\d{2})@thrive.ac.in$", message = "Must be well formed mail address.")
    private String email;
    @Password
    private String password;
}
