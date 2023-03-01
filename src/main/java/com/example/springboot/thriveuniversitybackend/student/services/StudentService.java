package com.example.springboot.thriveuniversitybackend.student.services;

import com.example.springboot.thriveuniversitybackend.Public.LoginDto;
import com.example.springboot.thriveuniversitybackend.enums.Attachments;
import com.example.springboot.thriveuniversitybackend.firebase.FileService;
import com.example.springboot.thriveuniversitybackend.student.dtos.PersonalInfoDto;
import com.example.springboot.thriveuniversitybackend.student.dtos.ProfileDto;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.PersonalInfo;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static com.example.springboot.thriveuniversitybackend.utils.RandomStringGenerator.generateRandomString;

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
    private FileService fileService;
    private static void setAttributesIfNull(PersonalInfo personalInfo, PersonalInfo updatedPersonalInfo) {
        if(updatedPersonalInfo.getFatherName() == null)
            updatedPersonalInfo.setFatherName(personalInfo.getFatherName());
        if(updatedPersonalInfo.getMotherName() == null)
            updatedPersonalInfo.setMotherName(personalInfo.getMotherName());
        if(updatedPersonalInfo.getDob() == null)
            updatedPersonalInfo.setDob(personalInfo.getDob());
        if(updatedPersonalInfo.getMobileNumber() == null)
            updatedPersonalInfo.setMobileNumber(personalInfo.getMobileNumber());
        if(updatedPersonalInfo.getSection() == null)
            updatedPersonalInfo.setSection(personalInfo.getSection());
        if(updatedPersonalInfo.getDepartment() == null)
            updatedPersonalInfo.setDepartment(personalInfo.getDepartment());
        if(updatedPersonalInfo.getEducationLevel() == null)
            updatedPersonalInfo.setEducationLevel(personalInfo.getEducationLevel());
        if(updatedPersonalInfo.getAddress() == null)
            updatedPersonalInfo.setAddress(personalInfo.getAddress());
    }

    private static ProfileDto transformtoProfileDto(Student student, ModelMapper modelMapper) {
        ProfileDto profile = modelMapper.map(student, ProfileDto.class);
        return profile;
    }

    private static PersonalInfo transformToPersonalInfo(PersonalInfoDto updatedPersonalInfoDto, ModelMapper modelMapper) {
        PersonalInfo updatedPersonalInfo = modelMapper.map(updatedPersonalInfoDto, PersonalInfo.class);
        return updatedPersonalInfo;
    }

    public StudentDto save(@Valid StudentDto studentdto) {
        Student student = transformtoStudent(studentdto);
        student.setPassword(generateRandomString(48, 122, 8));
        student.setPersonalInfo(new PersonalInfo());
        Student savedStudent = repository.save(student);
        studentdto.setId(savedStudent.getId());
        return studentdto;
    }

    public boolean isValidUser(LoginDto loginDto) {
        Student student = repository.findByEmail(loginDto.getEmail());
        return student.getPassword().equals(loginDto.getPassword());
    }

    public Student findStudentByEmail(String email){
        return repository.findByEmail(email);
    }

    public Student findStudentByRollNo(String rollNo){
        return repository.findByRollNo(rollNo);
    }

    public StudentDto transformtoStudentDto(Student student){
        return modelMapper.map(student, StudentDto.class);
    }

    public Student transformtoStudent(StudentDto studentDto){
        return modelMapper.map(studentDto, Student.class);
    }

    public void uploadProfilePicture(MultipartFile multipartFile, String rollNo, String attachmentType) throws IOException {
        fileService.upload(multipartFile, rollNo+"_"+attachmentType);
    }

    public String download(String objectId) throws IOException {
        return fileService.download(objectId);
    }

    public ProfileDto toProfileDto(String rollNo){
        Student student = findStudentByRollNo(rollNo);
        ProfileDto profile = transformtoProfileDto(student, modelMapper);
        try {
            profile.setProfilePictureURL(fileService.download(student.getRollNo()+"_"+Attachments.PROFILE_PICTURE.value));
        } catch (IOException e) {
            profile.setProfilePictureURL(null);
        }
        return profile;
    }

    public ProfileDto updateProfile(PersonalInfoDto updatedPersonalInfoDto, String rollNo) {
        PersonalInfo updatedPersonalInfo = transformToPersonalInfo(updatedPersonalInfoDto, modelMapper);
        Student student = repository.findByRollNo(rollNo);
        setAttributesIfNull(student.getPersonalInfo(), updatedPersonalInfo);
        student.setPersonalInfo(updatedPersonalInfo);
        Student updatedStudent  = repository.save(student);
        ProfileDto profile = transformtoProfileDto(updatedStudent, modelMapper);
        return profile;
    }


    private static void validate(PersonalInfo updatedPersonalInfo) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonalInfo>> violations = validator.validate(updatedPersonalInfo);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}


