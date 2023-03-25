package com.example.springboot.thriveuniversitybackend.student.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotAllowedToUseException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/uploadCertificate")
    public ResponseEntity uploadCertificate(@RequestParam("certificateType") String certificateType, @RequestParam("file") MultipartFile multipartFile, HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to update your profile picture !!");
        }
        String email = session.getAttribute("email").toString();
        String type = session.getAttribute("type").toString();
        if(!type.equals(UserTypes.STUDENT.name())){
            throw new UserNotAllowedToUseException("User is not allowed to perform the action!");
        }
        try {
            studentService.uploadCertificate(email, certificateType, multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully updated the certificates list", null));
    }

}
