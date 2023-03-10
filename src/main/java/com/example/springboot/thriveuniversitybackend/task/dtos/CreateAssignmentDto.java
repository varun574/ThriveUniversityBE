package com.example.springboot.thriveuniversitybackend.task.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateAssignmentDto {
    private String title;
    private String body;
    private String subjectName;
    private String year;
    private String semester;
    private String assignedToType;
    private String assignedTo;
    private String deadline;
}
