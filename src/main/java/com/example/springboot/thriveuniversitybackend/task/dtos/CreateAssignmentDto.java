package com.example.springboot.thriveuniversitybackend.task.dtos;


import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class CreateAssignmentDto {
    private String title;
    private String body;
    private String subjectName;
    private AcademicYear academicYear;
    private String assignedToType;
    private String assignedTo;
    private String deadline;
    private List<String> documentURLs;
}
