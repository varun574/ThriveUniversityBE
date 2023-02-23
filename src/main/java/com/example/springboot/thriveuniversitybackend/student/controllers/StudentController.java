package com.example.springboot.thriveuniversitybackend.student.controllers;

import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Student> getStudent(HttpSession session){
        if(session.isNew()) {
            throw new UserNotLoggedInException("Please log in to view your profile !!");
        }
        String rollNo = (String) session.getAttribute("rollNo");
        log.debug("Roll number : "+rollNo);
        Student student = studentService.findStudentByRollNo(rollNo);
        return ResponseEntity.ok(student);
    }

}
