package com.example.springboot.thriveuniversitybackend.task.dtos;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
public class SubmissionDto {
    private String id;
    private String rollNo;
    private String assignmentId;
    private String description;
    private List<Attachment> attachments;
    private String status;
    private List<String> comments;
    private LocalDate lastSubmittedOn;
}
