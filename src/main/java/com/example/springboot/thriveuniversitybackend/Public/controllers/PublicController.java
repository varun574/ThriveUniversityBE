package com.example.springboot.thriveuniversitybackend.Public.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.ForgotPasswordDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.LoginDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserAlreadyLoggedInException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.Public.models.User;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.otp.OTPExpiredException;
import com.example.springboot.thriveuniversitybackend.otp.OTPMismatchException;
import com.example.springboot.thriveuniversitybackend.otp.OTPService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PublicController {
    @Autowired
    private OTPService otpService;
    @Autowired
    private UserService userService;
    @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<SuccessResponseDto> login(@RequestBody @Valid LoginDto loginDto, HttpSession session){
        if(!userService.isValidUser(loginDto)) {
            throw new UserNotFoundException("Please enter valid credentials");
        }
        if(!session.isNew()){
            throw new UserAlreadyLoggedInException("You have already logged in.");
        }
        User user = userService.findUserByEmail(loginDto.getEmail());
        if(session.isNew()){
            session.setAttribute("name", user.getName());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("type", user.getType());
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Logged in Successfully!!", null));
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<SuccessResponseDto> forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto) {
        String email = forgotPasswordDto.getEmail();

        int otp = otpService.getOtp(email);
        if(otp == -1){
            throw new OTPExpiredException("OTP has been expired.");
        }
        if(otp != Integer.parseInt(forgotPasswordDto.getOtp())){
            throw new OTPMismatchException("Enter valid OTP");
        }
        User user = userService.findUserByEmail(email);
        userService.updatePassword(email, user.getPassword(), forgotPasswordDto.getNewPassword());
        return ResponseEntity.ok(new SuccessResponseDto<>("Password updated successfully", null));
    }
    @PostMapping("/generateOtp")
    public ResponseEntity<SuccessResponseDto> generateOtp(@RequestParam(name = "email") @Pattern(regexp = "^[a-z]+\\.[a-z]+@thrive.ac.in$", message = "Must be well formed mail address.")
                                          String email){
        userService.sendOtpMail(email);
        return ResponseEntity.ok(new SuccessResponseDto<>("OTP generated successfully. Please check your mail box.", null));
    }
}
