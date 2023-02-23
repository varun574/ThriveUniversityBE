package com.example.springboot.thriveuniversitybackend.common;

import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PublicController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<StudentDto> login(@RequestBody @Valid LoginDto loginDto, HttpSession session){
        if(!studentService.isValidUser(loginDto)) {
            throw new UserNotFoundException("Please enter valid credentials");
        }
        Student student = studentService.findStudentByEmail(loginDto.getEmail());
        if(session.isNew()){
            log.debug("Here creating session");
            session.setAttribute("rollNo", student.getRollNo());
            session.setAttribute("email", student.getEmail());
        }
        return ResponseEntity.ok(studentService.toStudentDto(student));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        session.invalidate();
        return ResponseEntity.ok("Successfully logged out");
    }
}
