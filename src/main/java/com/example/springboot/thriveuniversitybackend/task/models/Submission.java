package com.example.springboot.thriveuniversitybackend.task.models;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.enums.TaskStatus;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("submissions")
@Data
@Builder
public class Submission {
    @Id
    private String id;
    @NotEmpty
    @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number must be in specified format.")
    private String rollNo;
    @Indexed
    @NotEmpty
    private String assignmentId;
    private String description;
    private List<Attachment> attachments;
    @NotEmpty
    @EnumValue(enumC = TaskStatus.class, message = "Status must be chosen from the accepted values")
    private String status;
    private List<String> comments;
    private LocalDate lastSubmittedOn;

}
