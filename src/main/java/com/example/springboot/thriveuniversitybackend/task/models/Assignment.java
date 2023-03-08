package com.example.springboot.thriveuniversitybackend.task.models;

import com.example.springboot.thriveuniversitybackend.enums.AssignedToTypes;
import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Document("assignment")
@Data
@NoArgsConstructor
public class Assignment {
    @Id
    private String id;
    @NotEmpty
    private String title;
    private String body;
    private String subjectName;
    @Valid
    @NotNull
    private AcademicYear academicYear;
    private List<String> documentURLs;
    @Indexed
    @NotEmpty
    private String createdBy;
    @NotEmpty
    @EnumValue(enumC = AssignedToTypes.class, message = "Assignment should be assigned to the valid type.")
    private String assignedToType;
    @NotEmpty
    private String assignedTo;
    @Future
    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate deadline;
    @NotNull
    private LocalDate createdDate;
}
