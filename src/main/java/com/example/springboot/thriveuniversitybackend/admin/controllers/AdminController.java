package com.example.springboot.thriveuniversitybackend.admin.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UserRegisterDto;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.admin.dtos.PublicNotificationDto;
import com.example.springboot.thriveuniversitybackend.admin.dtos.SubjectDto;
import com.example.springboot.thriveuniversitybackend.admin.services.AdminService;
import com.example.springboot.thriveuniversitybackend.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/subjects")
    public ResponseEntity getSubjects(){
        List<SubjectDto> subjectDtos = adminService.getAllSubjects();
        return ResponseEntity.ok(new SuccessResponseDto<>("Subjects retrieved successfully", subjectDtos));
    }

    @PostMapping("/subject")
    public ResponseEntity addSubject(@RequestBody SubjectDto subjectDto){
        adminService.saveSubject(subjectDto);
        return ResponseEntity.ok(new SuccessResponseDto<>("Subject added successfully", null));
    }

    @PostMapping("/saveSubjects")
    public ResponseEntity addSubjects(@RequestBody List<SubjectDto> subjectDtos){
        adminService.saveSubjects(subjectDtos);
        return ResponseEntity.ok(new SuccessResponseDto<>("Subjects added successfully", null));
    }

    @PostMapping("/registerUser")
    public ResponseEntity registerUser(@RequestBody UserRegisterDto userRegisterDto){
        userService.saveUser(userRegisterDto);
        return ResponseEntity.ok("Successfully saved the user.");
    }

    @PostMapping("/postPublicNotification")
    public ResponseEntity postNotification(@RequestBody PublicNotificationDto publicNotificationDto){
        adminService.saveNotification(publicNotificationDto);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully saved the notification", null));
    }

    @GetMapping("/getLatestPublicNotifications")
    public ResponseEntity getLatestPublicNotifications(@RequestParam("limit") Long limit){
        List<PublicNotificationDto> publicNotificationDtos = adminService.getLatestNotifications(limit);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the latest notifications", publicNotificationDtos));
    }

}
