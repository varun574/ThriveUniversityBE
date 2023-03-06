package com.example.springboot.thriveuniversitybackend.admin;

import com.example.springboot.thriveuniversitybackend.Public.dtos.UserRegisterDto;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @PostMapping("/registerUser")
    public ResponseEntity registerUser(@RequestBody UserRegisterDto userRegisterDto){
        userService.saveUser(userRegisterDto);
        return ResponseEntity.ok("Successfully saved the user.");
    }
}
