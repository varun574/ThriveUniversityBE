package com.example.springboot.thriveuniversitybackend.student.services;

import com.example.springboot.thriveuniversitybackend.Public.models.User;
import com.example.springboot.thriveuniversitybackend.Public.services.UserService;
import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.enums.Certificates;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.repositories.StudentRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
@Transactional
@Slf4j
public class StudentService {

    @Autowired
    private StudentRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private FileService fileService;

    private HashMap<String, Integer> certificatesOrder;

    @PostConstruct
    public void init(){
        certificatesOrder = new HashMap<>();
        for (Certificates certificate: Certificates.values()
             ) {
            certificatesOrder.put(certificate.name(), certificate.ordinal());
        }
    }
    private static void setAttributesIfNull(Student student, Student updatedStudent) {
        if(updatedStudent.getUserId() == null)
            updatedStudent.setUserId(student.getUserId());
        if(updatedStudent.getRollNo() == null)
            updatedStudent.setRollNo(student.getRollNo());
        if(updatedStudent.getAcademicYear() == null)
            updatedStudent.setAcademicYear(student.getAcademicYear());
        if(updatedStudent.getPersonalEmail() == null)
            updatedStudent.setPersonalEmail(student.getPersonalEmail());
        if(updatedStudent.getFatherName() == null)
            updatedStudent.setFatherName(student.getFatherName());
        if(updatedStudent.getMotherName() == null)
            updatedStudent.setMotherName(student.getMotherName());
        if(updatedStudent.getDob() == null)
            updatedStudent.setDob(student.getDob());
        if(updatedStudent.getMobileNumber() == null)
            updatedStudent.setMobileNumber(student.getMobileNumber());
        if(updatedStudent.getSection() == null)
            updatedStudent.setSection(student.getSection());
        if(updatedStudent.getDepartment() == null)
            updatedStudent.setDepartment(student.getDepartment());
        if(updatedStudent.getEducationLevel() == null)
            updatedStudent.setEducationLevel(student.getEducationLevel());
        if(updatedStudent.getAddress() == null)
            updatedStudent.setAddress(student.getAddress());
        if(updatedStudent.getCertificates() == null)
            updatedStudent.setCertificates(student.getCertificates());
    }

    private static StudentDto transformtoStudentDto(Student student){
        return StudentDto.builder()
                .rollNo(student.getRollNo())
                .academicYear(student.getAcademicYear())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .dob(student.getDob())
                .personalEmail(student.getPersonalEmail())
                .department(student.getDepartment())
                .section(student.getSection())
                .educationLevel(student.getEducationLevel())
                .address(student.getAddress())
                .build();
    }

    private static Student transformtoStudent(StudentDto studentDto, ModelMapper modelMapper){
        return modelMapper.map(studentDto, Student.class);
    }

    public StudentDto updateProfileByUserId(StudentDto studentDto, String id) {
        Student student = repository.findByUserId(id);
        Student updatedStudent = transformtoStudent(studentDto, modelMapper);
        if(student == null){
            updatedStudent.setUserId(id);
            updatedStudent.setCertificates(new ArrayList<>(Certificates.values().length));
        }
        else {
            updatedStudent.setId(student.getId());
            setAttributesIfNull(student, updatedStudent);
        }
        updatedStudent = repository.save(updatedStudent);
        StudentDto updatedStudentDto = transformtoStudentDto(updatedStudent);
        return updatedStudentDto;
    }

    public void uploadCertificate(String email, String certificateType, MultipartFile multipartFile) throws IOException {
        User user = userService.findUserByEmail(email);
        Student student = repository.findByUserId(user.getId());
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        String hashedFileName = timeBasedGenerator.generate().toString() + "_" + AttachmentTypes.CERTIFICATE.name();
        fileService.upload(multipartFile, hashedFileName);
        Attachment updatedAttachment = Attachment.builder()
                .fileName(AttachmentTypes.CERTIFICATE.name())
                .hashedFileName(hashedFileName)
                .build();
        Attachment existingCertificate = student.getCertificates().get(certificatesOrder.get(certificateType));
        boolean result = fileService.delete(existingCertificate.getHashedFileName());
        if(!result)
            log.debug("Couldn't delete the existing certificate {}", existingCertificate.getHashedFileName());
        student.getCertificates().set(certificatesOrder.get(certificateType), updatedAttachment);
        repository.save(student);
    }

    public StudentDto getStudentByUserId(String id) {
        Student student = repository.findByUserId(id);
        StudentDto studentDto = transformtoStudentDto(student);
        return studentDto;
    }

    public String getPersonalEmailByUserId(String userId) {
        return repository.findPersonalEmailByUserId(userId).getPersonalEmail();
    }

    public List<String> getRollNosByAcademicDetails(AcademicYear academicYear, String department, String section) {
        return repository.findRollNosByAcademicDetails(academicYear, department, section).stream().map(Student::getRollNo).collect(Collectors.toList());
    }

    public List<String> getRollNosByDepartment(AcademicYear academicYear, List<String> departments) {
        return repository.findRollNosByDepartment(academicYear, departments).stream().map(Student::getRollNo).collect(Collectors.toList());
    }

    public boolean checkIfRollNumbersExist(List<String> pendingRollNos) {
        long count = repository.findAllByRollNo(pendingRollNos);
        return pendingRollNos.size() == count;
    }

    public String getRollNoByUserId(String userId) {
        Student student = repository.findByUserId(userId);
        return student.getRollNo();
    }


//    private static void validate(PersonalInfo updatedPersonalInfo) {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        Set<ConstraintViolation<PersonalInfo>> violations = validator.validate(updatedPersonalInfo);
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(violations);
//        }
//    }
}


