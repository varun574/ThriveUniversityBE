package com.example.springboot.thriveuniversitybackend.task.services;

import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserUnauthorizedException;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.enums.AssignedToTypes;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.enums.TaskStatus;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.exceptions.RollNumberDoesNotExistException;
import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.task.dtos.AssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.dtos.SubmissionDto;
import com.example.springboot.thriveuniversitybackend.task.exceptions.AssignmentNotFoundException;
import com.example.springboot.thriveuniversitybackend.task.exceptions.SubmissionNotFoundException;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.task.models.Submission;
import com.example.springboot.thriveuniversitybackend.task.repositories.AssignmentRepository;
import com.example.springboot.thriveuniversitybackend.task.repositories.SubmissionRepository;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private static AssignmentDto transformToAssignmentDto(Assignment assignment, ModelMapper modelMapper) {
        return modelMapper.map(assignment, AssignmentDto.class);
    }

    private static SubmissionDto transformToSubmissionDto(Submission submission, ModelMapper modelMapper) {
        return modelMapper.map(submission, SubmissionDto.class);
    }

    private static void setSignedUrls(Assignment assignment, FileService fileService) throws IOException {
        for (Attachment attachment: assignment.getAttachments()
        ) {
            String url = fileService.download(attachment.getHashedFileName());
            attachment.setUrl(url);
        }
    }

    private static void setAssignmentAttributesIfUpdated(Assignment assignment, CreateAssignmentDto updatedAssignment) {
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
        if(updatedAssignment.getSubjectName() != null)
            assignment.setSubjectName(updatedAssignment.getSubjectName());
        if(updatedAssignment.getAssignedToType() != null)
            assignment.setAssignedToType(updatedAssignment.getAssignedToType());
        if(updatedAssignment.getAssignedTo() != null)
            assignment.setAssignedTo(updatedAssignment.getAssignedTo());
    }

    private static void setSubmissionAttributesIfUpdated(Submission submission, SubmissionDto updatedSubmission) {
        if(updatedSubmission.getDescription() != null)
            submission.setDescription(updatedSubmission.getDescription());
        submission.setLastSubmittedOn(LocalDate.now());
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
    private List<String> getPendingRollNos(CreateAssignmentDto createAssignmentDto) {
        List<String> pendingRollNos = new ArrayList<>();
        String type = createAssignmentDto.getAssignedToType();
        if(type.equals(AssignedToTypes.STUDENT.name())){
            pendingRollNos = createAssignmentDto.getAssignedTo();
            if(!studentService.checkIfRollNumbersExist(pendingRollNos))
                throw new RollNumberDoesNotExistException("Given roll number/numbers doesn't exist");
        } else if (type.equals(AssignedToTypes.CLASS.name())) {
            for (String className: createAssignmentDto.getAssignedTo()
            ) {
                String[] splitString= className.split("-");
                String department = splitString[0], section = splitString[1];
                pendingRollNos.addAll(studentService.getRollNosByAcademicDetails(AcademicYear.builder()
                        .year(createAssignmentDto.getYear())
                        .semester(createAssignmentDto.getSemester())
                        .build(), department, section));
            }
        }
        else {
            List<String> departments = createAssignmentDto.getAssignedTo();
            pendingRollNos = studentService.getRollNosByDepartment(AcademicYear.builder()
                    .year(createAssignmentDto.getYear())
                    .semester(createAssignmentDto.getSemester())
                    .build(), departments);
        }
        return pendingRollNos;
    }
    private void createPendingSubmissionsForStudents(List<String> pendingRollNos, String assignmentId) {
        for (String rollNo: pendingRollNos
        ) {
            submissionRepository.save(Submission.builder()
                    .rollNo(rollNo)
                    .assignmentId(assignmentId)
                    .status(TaskStatus.PENDING.name())
                    .build());
        }
    }

    private void updateSubmissionRecordsForStudents(String assignmentId, CreateAssignmentDto createAssignmentDto) {
        List<String> prevRollNos = submissionRepository.findRollNosByAssignmentId(assignmentId).stream().map(Submission::getRollNo).toList();
        List<String> updatedRollNos = getPendingRollNos(createAssignmentDto);

        Set<String> prevRollNoSet = new HashSet<>(prevRollNos), updatedRollNoSet = new HashSet<>(updatedRollNos);
        List<String> deleteRollNos = new ArrayList<>(Sets.difference(prevRollNoSet, updatedRollNoSet)), appendRollNos = new ArrayList<>(Sets.difference(updatedRollNoSet, prevRollNoSet));

        submissionRepository.deleteAllByRollNo(assignmentId, deleteRollNos);
        createPendingSubmissionsForStudents(appendRollNos, assignmentId);
    }

    private AssignmentDto buildAssignmentDto(String type, Assignment assignment, String rollNo) {
        AssignmentDto assignmentDto = transformToAssignmentDto(assignment, modelMapper);
        assignmentDto.setTeacherEmail(userService.getEmailById(assignment.getCreatedBy()));
        if(type.equals(UserTypes.TEACHER.name()))
            assignmentDto.setSubmissions(submissionRepository.findAllByAssignmentId(assignment.getId()));
        else if (type.equals(UserTypes.STUDENT.name())) {
            assignmentDto.setSubmissions(Arrays.asList(submissionRepository.findByAssignmentIdAndRollNo(assignment.getId(), rollNo)));
        }
        return assignmentDto;
    }

    public void addTask(String email, CreateAssignmentDto createAssignmentDto, List<MultipartFile> multipartFiles) {
        List<Attachment> attachments;
        try {
            attachments = uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String userId = userService.getUserIdByEmail(email);
        createAssignmentDto.setAssignedTo(new ArrayList<>(Sets.newHashSet(createAssignmentDto.getAssignedTo())));
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
        Assignment savedAssignment = assignmentRepository.save(assignment);
        List<String> pendingRollNos = getPendingRollNos(createAssignmentDto);
        createPendingSubmissionsForStudents(pendingRollNos, savedAssignment.getId());
    }

    public List<AssignmentDto> getAssignments(String email, String type) throws IOException {
        String userId = userService.getUserIdByEmail(email);
        String rollNo = null;
        List<Assignment> assignments = null;
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto student = studentService.getStudentByUserId(userId);
            rollNo = student.getRollNo();
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
        List<AssignmentDto> assignmentDtos = new ArrayList<>();
        if(assignments != null) {
            for (Assignment assignment : assignments
            ) {
                assignmentDtos.add(buildAssignmentDto(type, assignment, rollNo));
            }
        }
        return assignmentDtos;
    }

    public AssignmentDto getAssignment(String email, String type, String assignmentId) throws IOException {
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = null;
        String rollNo = null;
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto student = studentService.getStudentByUserId(userId);
            rollNo = student.getRollNo();
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
        return buildAssignmentDto(type, assignment, rollNo);
    }

    public AssignmentDto updateTask(String email, String assignmentId, CreateAssignmentDto createAssignmentDto, List<MultipartFile> multipartFiles) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Assignment> assignmentResult = assignmentRepository.findById(assignmentId);
        assignmentResult.orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found"));
        Assignment assignment = assignmentResult.get();
        if(!assignment.getCreatedBy().equals(userId))
            throw new UserUnauthorizedException("User is not authorized to update the requested task.");
        if(assignment.getAttachments() != null){
            List<String> hashedFileNames = assignment.getAttachments().stream().map(Attachment::getHashedFileName).toList();
            List<String> result = fileService.deleteMultiple(hashedFileNames);
            log.debug("Undeleted files {}", result);
        }
        List<Attachment> attachments;
        try {
            attachments = uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createAssignmentDto.setAssignedTo(new ArrayList<>(Sets.newHashSet(createAssignmentDto.getAssignedTo())));
        setAssignmentAttributesIfUpdated(assignment, createAssignmentDto);
        updateSubmissionRecordsForStudents(assignmentId, createAssignmentDto);
        assignment.setAttachments(attachments);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        return buildAssignmentDto(UserTypes.TEACHER.name(), savedAssignment, null);
    }

    public void deleteTask(String email, String assignmentId) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Assignment> assignmentResult = assignmentRepository.findById(assignmentId);
        assignmentResult.orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found"));
        Assignment assignment = assignmentResult.get();
        if(!assignment.getCreatedBy().equals(userId))
            throw new UserUnauthorizedException("User is not authorized to delete the requested task.");
        List<String> hashedFileNames = assignment.getAttachments().stream().map(Attachment::getHashedFileName).toList();
        List<String> result = fileService.deleteMultiple(hashedFileNames);
        log.debug("Undeleted files {}", result);
        submissionRepository.deleteAllByAssignmentId(assignmentId);
        assignmentRepository.delete(assignment);
    }

    public SubmissionDto getStudentSubmission(String email, String assignmentId, String type, String rollNo) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Assignment> result = assignmentRepository.findById(assignmentId);
        result.orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found."));
        Assignment assignment = result.get();
        if(type.equals(UserTypes.TEACHER.name())){
            if(!userId.equals(assignment.getCreatedBy()))
                throw new UserUnauthorizedException("You are unauthorized to view the submission.");
        } else if (type.equals((UserTypes.STUDENT.name()))) {
            String crollNo = studentService.getRollNoByUserId(userId);
            if(!crollNo.equals(rollNo))
                throw new UserUnauthorizedException("You are unauthorized to view the submission.");
        }
        else {
            //admin
        }
        Submission submission = submissionRepository.findByAssignmentIdAndRollNo(assignmentId, rollNo);
        if(submission == null)
            throw new SubmissionNotFoundException("Cannot fetch the submission as requested roll number is not assigned to the assignment.");
        return transformToSubmissionDto(submission, modelMapper);
    }

    public SubmissionDto updateStudentSubmission(String email, String submissionId, SubmissionDto submissionDto, List<MultipartFile> multipartFiles) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Submission> submissionResult = submissionRepository.findById(submissionId);
        submissionResult.orElseThrow(() -> new SubmissionNotFoundException("Requested submission is not found."));
        Submission submission = submissionResult.get();
        String rollNo = studentService.getRollNoByUserId(userId);
        if(!submission.getRollNo().equals(rollNo)){
            throw new UserUnauthorizedException("You are unauthorized to update the submission");
        }
        if(submission.getAttachments() != null){
            List<String> hashedFileNames = submission.getAttachments().stream().map(Attachment::getHashedFileName).toList();
            List<String> result = fileService.deleteMultiple(hashedFileNames);
            log.debug("Undeleted files {}", result);
        }
        List<Attachment> attachments;
        try {
            attachments = uploadTaskAttachments(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        submission.setAttachments(attachments);
        setSubmissionAttributesIfUpdated(submission, submissionDto);
        submissionRepository.save(submission);
        return transformToSubmissionDto(submission, modelMapper);
    }

    public SubmissionDto reviewSubmission(String email, String assignmentId, String rollNo, String status) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Assignment> assignmentResult = assignmentRepository.findById(assignmentId);
        assignmentResult.orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found"));
        Assignment assignment = assignmentResult.get();
        if(!assignment.getCreatedBy().equals(userId))
            throw new UserUnauthorizedException("User is not authorized to update the requested task.");
        Submission submission = submissionRepository.findByAssignmentIdAndRollNo(assignmentId, rollNo);
        if(submission == null)
            throw new SubmissionNotFoundException("Requested submission for the roll number is not found.");
        submission.setStatus(status);
        submissionRepository.save(submission);
        return transformToSubmissionDto(submission, modelMapper);
    }

    public SubmissionDto updateSubmissionComments(String email, String assignmentId, String rollNo, List<String> comments) {
        String userId = userService.getUserIdByEmail(email);
        Optional<Assignment> assignmentResult = assignmentRepository.findById(assignmentId);
        assignmentResult.orElseThrow(() -> new AssignmentNotFoundException("Assignment is not found"));
        Assignment assignment = assignmentResult.get();
        if(!assignment.getCreatedBy().equals(userId))
            throw new UserUnauthorizedException("User is not authorized to update the requested task.");
        Submission submission = submissionRepository.findByAssignmentIdAndRollNo(assignmentId, rollNo);
        if(submission == null)
            throw new SubmissionNotFoundException("Requested submission for the roll number is not found.");
        submission.setComments(comments);
        submissionRepository.save(submission);
        return transformToSubmissionDto(submission, modelMapper);
    }
}
