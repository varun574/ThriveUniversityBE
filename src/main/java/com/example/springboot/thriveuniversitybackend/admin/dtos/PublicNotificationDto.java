package com.example.springboot.thriveuniversitybackend.admin.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicNotificationDto {
    private String title;
    private String body;
}
