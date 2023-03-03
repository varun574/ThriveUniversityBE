package com.example.springboot.thriveuniversitybackend.Public;

import com.example.springboot.thriveuniversitybackend.otp.OTPExpiredException;
import com.example.springboot.thriveuniversitybackend.otp.OTPMismatchException;
import com.example.springboot.thriveuniversitybackend.otp.OTPService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PublicController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/login")
    public ResponseEntity<StudentDto> login(@RequestBody @Valid LoginDto loginDto, HttpSession session){
        if(!studentService.isValidUser(loginDto)) {
            throw new UserNotFoundException("Please enter valid credentials");
        }
        Student student = studentService.findStudentByEmail(loginDto.getEmail());
        if(session.isNew()){
            session.setAttribute("rollNo", student.getRollNo());
            session.setAttribute("email", student.getEmail());
        }
        return ResponseEntity.ok(studentService.transformtoStudentDto(student));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        session.invalidate();
        return ResponseEntity.ok("Successfully logged out");
    }

    @PostMapping("/generateOtp")
    public ResponseEntity generateOtp(@RequestParam(name = "rollNo") @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$",
            message = "Roll number must be in specified format.") String rollNo){
        Student student = studentService.findStudentByRollNo(rollNo);
        if(student == null)
            throw new UserNotFoundException("User not found, please try again with valid roll number");
        otpService.sendOtpMail(student.getPersonalInfo().getPersonalEmail(), rollNo);
        return ResponseEntity.ok("OTP generated successfully. Please check your mail box.");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto) throws OTPExpiredException {
        String rollNo = forgotPasswordDto.getRollNo();

        int otp = otpService.getOtp(rollNo);
        if(otp == -1){
            throw new OTPExpiredException("OTP has been expired.");
        }
        if(otp != Integer.parseInt(forgotPasswordDto.getOtp())){
            throw new OTPMismatchException("Enter valid OTP");
        }
        Student student = studentService.findStudentByRollNo(rollNo);
        studentService.updatePassword(rollNo, student.getPassword(), forgotPasswordDto.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}
