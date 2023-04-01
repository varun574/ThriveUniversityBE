package com.example.springboot.thriveuniversitybackend.Public.models;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "admissions")
@Data
@Builder
public class Admission {
    @Id
    private String id;
    private String data;
    private String trackingId;
    private String status;
}
