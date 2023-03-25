package com.example.springboot.thriveuniversitybackend.admin.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("public_notifications")
@Data
@NoArgsConstructor
public class PublicNotification {
    @Id
    private String id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String body;
    private LocalDateTime createdOn;
}
