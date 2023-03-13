package com.example.springboot.thriveuniversitybackend.task.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class CreateAssignmentDto {
    private String title;
    private String body;
    private String subjectName;
    private String year;
    private String semester;
    private String assignedToType;
    private List<String> assignedTo;
    private String deadline;
}
