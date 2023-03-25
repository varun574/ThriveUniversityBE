package com.example.springboot.thriveuniversitybackend.student.models;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.enums.EducationLevel;
import com.example.springboot.thriveuniversitybackend.enums.Section;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.example.springboot.thriveuniversitybackend.validators.annotations.NullableNonEmpty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("students")
@Data
@NoArgsConstructor
public class Student {
    @Id
    private String id;
    @NotEmpty(message = "Roll number must not be empty")
    @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number must be in specified format.")
    private String rollNo;
    @Valid
    private AcademicYear academicYear;
    @Indexed
    @NotEmpty(message = "User Id must not be empty")
    private String userId;
    @NullableNonEmpty(message = "Father name must not be empty")
    private String fatherName;
    @NullableNonEmpty(message = "Mother name must not be empty")
    private String motherName;
    @Past(message = "Earlier date must be picked")
    private LocalDate dob;
    @Email
    @NotEmpty(message = "Personal Email must not be empty.")
    private String personalEmail;
    @NullableNonEmpty(message = "Mobile number must not be empty")
    @Pattern(regexp = "^(?:\\+91)?\\d{10}$", message = "Mobile must be valid phone number.")
    private String mobileNumber;

    @NullableNonEmpty(message = "Department must not be empty")
    @EnumValue(enumC = Department.class, message = "Department must be chosen from the given accepted values")
    private String department;

    @NullableNonEmpty(message = "Section must not be empty")
    @EnumValue(enumC = Section.class, message = "Section must be chosen from the given accepted values")
    private String section;

    @EnumValue(enumC = EducationLevel.class, message = "Education Level must be chosen from the given accepted values")
    @NullableNonEmpty(message = "Education Level must not be empty")
    private String educationLevel;
    @NullableNonEmpty(message = "Address must not be empty")
    private String address;
    private List<Attachment> certificates;
}
