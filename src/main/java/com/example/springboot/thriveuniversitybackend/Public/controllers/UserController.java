package com.example.springboot.thriveuniversitybackend.Public.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.UpdateProfileDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.otp.OTPService;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UpdatePasswordDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto> logout(HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        session.invalidate();
        return ResponseEntity.ok(new SuccessResponseDto("Logged out Successfully!!", null));
    }

    @GetMapping("/profile")
    public ResponseEntity getProfile(HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type =(String) session.getAttribute("type");
        String email =(String) session.getAttribute("email");
        Object profile = userService.getProfile(type, email);
        return ResponseEntity.ok(new SuccessResponseDto("Fetched profile successfully", profile));
    }

    @PostMapping("/profile")
    public ResponseEntity updateProfile(@RequestBody UpdateProfileDto updateProfileDto, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type =(String) session.getAttribute("type");
        String email =(String) session.getAttribute("email");
        Object updateProfile = userService.updateProfile(type, email, updateProfileDto);
        return ResponseEntity.ok(new SuccessResponseDto("Updated Profile Successfully", updateProfile));
    }

    @PostMapping(value = "/uploadProfilePicture")
    public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile, HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to update your profile picture !!");
        }
        try {
            String email = session.getAttribute("email").toString();
            userService.uploadProfilePicture(email, multipartFile, AttachmentTypes.PROFILE_PICTURE.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto("Successfully Uploaded Profile Picture", null));
    }

    @PostMapping("/updatePassword")
    public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto, HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to update your profile picture !!");
        }
        String email = session.getAttribute("email").toString();
        userService.updatePassword(email, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword());
        return ResponseEntity.ok(new SuccessResponseDto("Successfully updated password", null));
    }

}
