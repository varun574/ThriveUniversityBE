package com.example.springboot.thriveuniversitybackend.teacher.models;

import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("teacher")
@Data
@NoArgsConstructor
public class Teacher {
    @Id
    private String id;
    @Indexed
    private String userId;
    @NullableNonEmpty(message = "Mobile number must not be empty")
    @Pattern(regexp = "^(?:\\+91)?\\d{10}$", message = "Mobile must be valid phone number.")
    private String mobileNumber;
    @Email
    @NotEmpty(message = "Personal Email must not be empty.")
    private String personalEmail;
    @Enumerated(EnumType.STRING)
    @NullableNonEmpty(message = "Department must not be empty")
    @EnumValue(enumC = Department.class, message = "Department must be chosen from the given accepted values")
    private String department;
    @NullableNonEmpty(message = "Address must not be empty")
    private String address;
}
