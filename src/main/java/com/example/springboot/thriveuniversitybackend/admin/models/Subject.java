package com.example.springboot.thriveuniversitybackend.admin.models;

import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("subjects")
@Data
@NoArgsConstructor
public class Subject {
    @Id
    private String id;
    @NotEmpty
    @Indexed(unique = true)
    private String subjectName;
    @NotEmpty
    @EnumValue(enumC = Department.class, message = "Department must be chosen from the given accepted values")
    private String department;
}
