package com.example.springboot.thriveuniversitybackend.admin;

import com.example.springboot.thriveuniversitybackend.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private MailService mailService;
}
