package com.example.springboot.thriveuniversitybackend.student.controllers;

import com.example.springboot.thriveuniversitybackend.enums.Attachments;
import com.example.springboot.thriveuniversitybackend.student.dtos.PersonalInfoDto;
import com.example.springboot.thriveuniversitybackend.student.dtos.ProfileDto;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    @PostMapping("/register")
    public ResponseEntity<StudentDto> registerStudent(@RequestBody @Valid StudentDto student){
        StudentDto registeredStudent = studentService.save(student);
        return ResponseEntity.ok(registeredStudent);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getStudent(HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to view your profile !!");
        }
        String rollNo = (String) session.getAttribute("rollNo");
        log.debug("Roll number : "+rollNo);
        ProfileDto profile = studentService.toProfileDto(rollNo);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/profile")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody PersonalInfoDto personalInfoDto, HttpSession session) {
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to view your profile !!");
        }
        System.out.println(personalInfoDto);
        String rollNo = (String) session.getAttribute("rollNo");
        ProfileDto profile = studentService.updateProfile(personalInfoDto, rollNo);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile, HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to view your profile !!");
        }
        try {
            String rollNo = session.getAttribute("rollNo").toString();
            studentService.uploadProfilePicture(multipartFile, rollNo, Attachments.PROFILE_PICTURE.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Uploaded Successfully");
    }

    @GetMapping("/download")
    public  ResponseEntity download(){
        String downloadURL = null;
        String objectId = "06a8a016-c143-48e9-b587-cec812f40ee3.jpg";
        try {
            downloadURL = studentService.download(objectId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(downloadURL);
    }

}
