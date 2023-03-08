package com.example.springboot.thriveuniversitybackend.task.services;

import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.enums.Attachments;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.firebase.FileService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.task.dtos.CreateAssignmentDto;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import com.example.springboot.thriveuniversitybackend.task.repositories.AssignmentRepository;
import com.example.springboot.thriveuniversitybackend.task.repositories.SubmissionRepository;
import com.example.springboot.thriveuniversitybackend.teacher.services.TeacherService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    public List<String> uploadTaskAttachments(MultipartFile[] multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        for (MultipartFile file: multipartFiles
        ) {
            String filename = timeBasedGenerator.generate().toString()+ Attachments.ASSIGNMENT.name();
            fileService.upload(file, filename);
            filenames.add(filename);
        }
        return filenames;
    }

    public void addTask(CreateAssignmentDto createAssignmentDto, String email) {
        String userId = userService.getUserIdByEmail(email);
        Assignment assignment = modelMapper.map(createAssignmentDto, Assignment.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        assignment.setDeadline(LocalDate.parse(createAssignmentDto.getDeadline(),formatter));
        assignment.setCreatedBy(userId);
        assignment.setCreatedDate(LocalDate.now());
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
                assignment.setDocumentURLs(fileService.downloadMultiple(assignment.getDocumentURLs()));
            }
        }
        return assignments;
    }
}
