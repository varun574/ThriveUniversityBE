package com.example.springboot.thriveuniversitybackend.student.dtos;

import com.example.springboot.thriveuniversitybackend.validators.annotations.FieldsValueMatch;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@FieldsValueMatch(compareField = "confirmPassword", targetField = "newPassword", message = "Passwords do not match!")
public class UpdatePasswordDto {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
}
