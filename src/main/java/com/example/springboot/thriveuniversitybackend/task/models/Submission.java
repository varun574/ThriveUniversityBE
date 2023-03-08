package com.example.springboot.thriveuniversitybackend.task.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("submissions")
@Data
@NoArgsConstructor
public class Submission {
    @Id
    private String id;
    @NotEmpty
    private String studentId;
    @Indexed
    @NotEmpty
    private String assignmentId;
    @NotEmpty
    private List<String> documentURLs;
    @NotEmpty
    private LocalDate submittedOn;
}
