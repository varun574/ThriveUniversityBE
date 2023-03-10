package com.example.springboot.thriveuniversitybackend.task.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.ErrorResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserUnauthorizedException;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.task.repositories.AssignmentRepository;
import com.example.springboot.thriveuniversitybackend.task.services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("/task")
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @GetMapping("/assignments")
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

    @GetMapping("/assignments/{id}")
    public ResponseEntity getAssignment(@PathVariable("id") String assignmentId, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        String email = session.getAttribute("email").toString();
        Assignment assignment = null;
        try {
            assignment = taskService.getAssignment(email, type, assignmentId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the assignment", assignment));
    }

    @PostMapping(value = "/assignments")
    public ResponseEntity addTask(@ModelAttribute CreateAssignmentDto createAssignmentDto, @RequestParam(value = "documents", required = false) List<MultipartFile> multipartFiles, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            throw new UserUnauthorizedException("You are not authorized to create an assignment.");
        }
        String email = session.getAttribute("email").toString();
        taskService.addTask(email, createAssignmentDto, multipartFiles);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully added the task", null));
    }

    @PutMapping(value = "/assignments/{id}")
    public ResponseEntity updateAssignment(@ModelAttribute CreateAssignmentDto createAssignmentDto,
                                           @RequestParam("documents") List<MultipartFile> files,
                                           @PathVariable("id") String assignmentId,
                                           HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            throw new UserUnauthorizedException("You are not authorized to update an assignment.");
        }
        String email = session.getAttribute("email").toString();
        Assignment updatedAssignment = taskService.updateTask(email, assignmentId, createAssignmentDto, files);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully updated the task", updatedAssignment));
    }

    @DeleteMapping(value = "/assignments/{id}")
    public ResponseEntity deleteAssignment(@PathVariable("id") String assignmentId, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            return ResponseEntity.ok(new ErrorResponseDto("You are not authorized to delete an assignment.", HttpStatus.UNAUTHORIZED.value(), null));
        }
        String email = session.getAttribute("email").toString();
        taskService.deleteTask(email, assignmentId);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully deleted the task", null));
    }
}
