package com.example.springboot.thriveuniversitybackend.task.dtos;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.task.models.Submission;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssignmentDto {
    private String id;
    private String title;
    private String body;
    private String subjectName;
    private AcademicYear academicYear;
    private List<Attachment> attachments;
    private String createdBy;
    private String teacherEmail;
    private String assignedToType;
    private List<String> assignedTo;
    private LocalDate deadline;
    private LocalDate createdDate;
    private List<Submission> submissions;

}
