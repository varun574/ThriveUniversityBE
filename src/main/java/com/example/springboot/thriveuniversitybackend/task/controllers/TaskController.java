package com.example.springboot.thriveuniversitybackend.task.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.task.repositories.AssignmentRepository;
import com.example.springboot.thriveuniversitybackend.task.services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RequestMapping("/task")
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @PostMapping(value = "/assignments")
    public ResponseEntity addTask(@RequestBody CreateAssignmentDto createAssignmentDto, HttpSession session){
        System.out.println(createAssignmentDto);
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String email = session.getAttribute("email").toString();
        taskService.addTask(createAssignmentDto, email);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully added the task", null));
    }

    @PostMapping("/uploadTaskAttachment")
    public ResponseEntity uploadTask(@RequestParam("files") MultipartFile[] multipartFiles, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        List<String> filenames;
        try {
            filenames = taskService.uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, List> data = new HashMap<>();
        data.put("filenames", filenames);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully uploaded the files", data));
    }

    @GetMapping("/getAssignments")
    public ResponseEntity getAssignments(HttpSession session){
        String email = session.getAttribute("email").toString();
        String type = session.getAttribute("type").toString();
        List<Assignment> assignments = null;
        try {
            assignments = taskService.getAssignments(email,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the assignments", assignments));
    }
}
