package com.example.springboot.thriveuniversitybackend.task.services;

import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserUnauthorizedException;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.task.repositories.AssignmentRepository;
import com.example.springboot.thriveuniversitybackend.task.repositories.SubmissionRepository;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StudentService studentService;

    private static void setSignedUrls(Assignment assignment, FileService fileService) throws IOException {
        for (Attachment attachment: assignment.getAttachments()
        ) {
            String url = fileService.download(attachment.getHashedFileName());
            attachment.setUrl(url);
        }
    }

    private static void setAttributesIfUpdated(Assignment assignment, CreateAssignmentDto updatedAssignment) {
        if(updatedAssignment.getTitle() != null)
            assignment.setTitle(updatedAssignment.getTitle());
        if(updatedAssignment.getBody() != null)
            assignment.setBody(updatedAssignment.getBody());
        if(updatedAssignment.getDeadline() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            assignment.setDeadline(LocalDate.parse(updatedAssignment.getDeadline(), formatter));
        }
        if(updatedAssignment.getSemester()!=null || updatedAssignment.getYear()!=null){
            assignment.setAcademicYear(AcademicYear.builder()
                    .semester(updatedAssignment.getSemester())
                    .year(updatedAssignment.getYear())
                    .build());
        }
        if(updatedAssignment.getAssignedTo() != null)
            assignment.setAssignedTo(updatedAssignment.getAssignedTo());
        if(updatedAssignment.getAssignedToType() != null)
            assignment.setAssignedToType(updatedAssignment.getAssignedToType());
        if(updatedAssignment.getSubjectName() != null)
            assignment.setSubjectName(updatedAssignment.getSubjectName());
    }


    public List<Attachment> uploadTaskAttachments(List<MultipartFile> multipartFiles) throws IOException {
        List<Attachment> attachments = new ArrayList<>();
        if(multipartFiles==null || multipartFiles.isEmpty())
            return attachments;
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        for (MultipartFile file: multipartFiles
        ) {
            if(file.getSize()>0) {
                String hashedFileName = timeBasedGenerator.generate().toString() + "_" + AttachmentTypes.ASSIGNMENT.name();
                fileService.upload(file, hashedFileName);
                attachments.add(Attachment.builder()
                        .fileName(file.getOriginalFilename())
                        .hashedFileName(hashedFileName)
                        .build());
            }
        }
        return attachments;
    }

    public void addTask(String email, CreateAssignmentDto createAssignmentDto, List<MultipartFile> multipartFiles) {
        List<Attachment> attachments;
        try {
            attachments = uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = modelMapper.map(createAssignmentDto, Assignment.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        assignment.setDeadline(LocalDate.parse(createAssignmentDto.getDeadline(),formatter));
        assignment.setCreatedBy(userId);
        assignment.setCreatedDate(LocalDate.now());
        assignment.setAttachments(attachments);
        assignment.setAcademicYear(AcademicYear.builder()
                        .semester(createAssignmentDto.getSemester())
                        .year(createAssignmentDto.getYear())
                        .build());
        assignmentRepository.save(assignment);
    }
    public List<Assignment> getAssignments(String email, String type) throws IOException {
        String userId = userService.getUserIdByEmail(email);
        List<Assignment> assignments = null;
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto student = studentService.getStudentByUserId(userId);
            String className = student.getDepartment()+"-"+student.getSection();
            assignments = assignmentRepository.findAllByStudentDetails(userId, student.getAcademicYear(), student.getDepartment(), className);
        }
        else if (type.equals(UserTypes.TEACHER.name())){
            assignments = assignmentRepository.findByCreatedBy(userId);
        }else{
            //admin
        }
        if(assignments!=null) {
            for (Assignment assignment : assignments) {
                setSignedUrls(assignment, fileService);
            }
        }
        return assignments;
    }

    public Assignment updateTask(String email, String assignmentId, CreateAssignmentDto createAssignmentDto, List<MultipartFile> multipartFiles) {
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = assignmentRepository.findByIdAndCreatedBy(assignmentId, userId);
        if(assignment == null)
            throw new UserUnauthorizedException("User is not authorized to update the requested task.");
        List<String> hashedFileNames = assignment.getAttachments().stream().map(Attachment::getHashedFileName).toList();
        List<String> result = fileService.deleteMultiple(hashedFileNames);
        log.debug("Undeleted files {}", result);
        List<Attachment> attachments;
        try {
            attachments = uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setAttributesIfUpdated(assignment, createAssignmentDto);
        assignment.setAttachments(attachments);
        assignmentRepository.save(assignment);
        return assignment;
    }

    public void deleteTask(String email, String assignmentId) {
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = assignmentRepository.findByIdAndCreatedBy(assignmentId, userId);
        if(assignment == null)
            throw new UserUnauthorizedException("User is not authorized to delete the requested task.");
        List<String> hashedFileNames = assignment.getAttachments().stream().map(Attachment::getHashedFileName).toList();
        List<String> result = fileService.deleteMultiple(hashedFileNames);
        log.debug("Undeleted files {}", result);
        assignmentRepository.delete(assignment);
    }

    public Assignment getAssignment(String email, String type, String assignmentId) throws IOException {
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = null;
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto student = studentService.getStudentByUserId(userId);
            String className = student.getDepartment()+"-"+student.getSection();
            assignment = assignmentRepository.findByIdAndStudentDetails(assignmentId, userId, student.getAcademicYear(), student.getDepartment(), className);
        }
        else if (type.equals(UserTypes.TEACHER.name())){
            assignment = assignmentRepository.findByIdAndCreatedBy(assignmentId, userId);
        }else{
            //admin
        }
        if(assignment!=null) {
            setSignedUrls(assignment, fileService);
        }
        else {
            throw new UserUnauthorizedException("You neither created nor been assigned to the assignment to get the details.");
        }
        return assignment;
    }
}
