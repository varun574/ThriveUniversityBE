package com.example.springboot.thriveuniversitybackend.task.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.SuccessResponseDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotLoggedInException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserUnauthorizedException;
import com.example.springboot.thriveuniversitybackend.enums.TaskStatus;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.task.dtos.AssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.dtos.SubmissionDto;
import com.example.springboot.thriveuniversitybackend.task.services.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("/task")
@RestController
@Validated
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/assignments")
    public ResponseEntity getAssignments(HttpSession session){
        String email = session.getAttribute("email").toString();
        String type = session.getAttribute("type").toString();
        List<AssignmentDto> assignmentDtos = null;
        try {
            assignmentDtos = taskService.getAssignments(email,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the assignments", assignmentDtos));
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity getAssignment(@PathVariable("id") String assignmentId, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        String email = session.getAttribute("email").toString();
        AssignmentDto assignmentDto = null;
        try {
            assignmentDto = taskService.getAssignment(email, type, assignmentId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the assignment", assignmentDto));
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
        AssignmentDto updatedAssignmentDto = taskService.updateTask(email, assignmentId, createAssignmentDto, files);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully updated the task", updatedAssignmentDto));
    }

    @DeleteMapping(value = "/assignments/{id}")
    public ResponseEntity deleteAssignment(@PathVariable("id") String assignmentId, HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            throw new UserUnauthorizedException("You are not authorized to delete an assignment.");
        }
        String email = session.getAttribute("email").toString();
        taskService.deleteTask(email, assignmentId);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully deleted the task", null));
    }
    
    @GetMapping("/assignments/{id}/submission/{rollNo}")
    public ResponseEntity getStudentSubmission(@PathVariable("id") String assignmentId,
                                               @PathVariable("rollNo") @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number must be in specified format.") String rollNo,
                                               HttpSession session){
        if(session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        String email = session.getAttribute("email").toString();
        SubmissionDto submissionDto = taskService.getStudentSubmission(email, assignmentId, type, rollNo);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully retrieved the submission", submissionDto));
    }

    @PutMapping("/submissions/{id}")
    public ResponseEntity updateStudentSubmission(@PathVariable("id") String submissionId,
                                                  @ModelAttribute SubmissionDto submissionDto,
                                                  @RequestParam("documents") List<MultipartFile> files,
                                                  HttpSession session){
        if (session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.TEACHER.name())){
            throw new UserUnauthorizedException("You are not authorized to update an assignment submission.");
        }
        String email = session.getAttribute("email").toString();
        SubmissionDto updateStudentSubmissionDto = taskService.updateStudentSubmission(email, submissionId, submissionDto, files);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully updated the submission", updateStudentSubmissionDto));
    }

    @PostMapping("/assignments/{assignmentId}/submissions/{rollNo}/review")
    public ResponseEntity addComment(@PathVariable("assignmentId") String assignmentId,
                                     @PathVariable("rollNo") @Pattern(regexp = "^\\d{2}B81A\\d{2}(\\w\\d|\\d{2})$", message = "Roll number must be in specified format.") String rollNo,
                                     @RequestParam("status") String status,
                                     HttpSession session){
        if (session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            throw new UserUnauthorizedException("You are not authorized to approve or reject an assignment submission.");
        }
        String email = session.getAttribute("email").toString();
        SubmissionDto submissionDto = taskService.reviewSubmission(email, assignmentId, rollNo, status);
        return ResponseEntity.ok(new SuccessResponseDto<>("The submission status is successfully updated as "+status, submissionDto));
    }

    @PostMapping("/assignments/{assignmentId}/submissions/{rollNo}/comment")
    public ResponseEntity commentOnStudentSubmission(@PathVariable("assignmentId") String assignmentId,
                                                     @PathVariable("rollNo") String rollNo,
                                                     @RequestParam("comments") List<String> comments,
                                                     HttpSession session){
        if (session.isNew())
            throw new UserNotLoggedInException("Please log in!!");
        String type = session.getAttribute("type").toString();
        if(type.equals(UserTypes.STUDENT.name())){
            throw new UserUnauthorizedException("You are not authorized to approve or reject an assignment submission.");
        }
        String email = session.getAttribute("email").toString();
        SubmissionDto submissionDto = taskService.updateSubmissionComments(email, assignmentId, rollNo, comments);
        return ResponseEntity.ok(new SuccessResponseDto<>("Successfully updated the comments on the submission", submissionDto));
    }

}
