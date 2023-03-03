package com.example.springboot.thriveuniversitybackend.Public;

import com.example.springboot.thriveuniversitybackend.validators.annotations.FieldsValueMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldsValueMatch(compareField = "confirmPassword", targetField = "newPassword", message = "Passwords do not match!")
public class ForgotPasswordDto {
    @Pattern(regexp = "^\\d{4}$", message = "OTP must be a number")
    private String otp;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
    @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number must be in specified format.")
    private String rollNo;
}
