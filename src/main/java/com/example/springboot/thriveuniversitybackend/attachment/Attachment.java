package com.example.springboot.thriveuniversitybackend.attachment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attachment {
    private String fileName;

    private String hashedFileName;
    private String url;

}
