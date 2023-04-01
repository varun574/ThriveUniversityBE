package com.example.springboot.thriveuniversitybackend.Public.dtos;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import lombok.Data;

@Data
public class ExamDetailsDto {
    private String exam_name;
    private String score;
    private String additional_data;
    private Attachment uploaded_score;
}
