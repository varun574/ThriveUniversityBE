package com.example.springboot.thriveuniversitybackend.student.services;

import com.example.springboot.thriveuniversitybackend.firebase.FileService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;



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
    private static void setAttributesIfNull(Student student, Student updatedStudent) {
        if(updatedStudent.getUserId() == null)
            updatedStudent.setUserId(student.getUserId());
        if(updatedStudent.getRollNo() == null)
            updatedStudent.setRollNo(student.getRollNo());
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
            student.setSection(student.getSection());
        if(updatedStudent.getDepartment() == null)
            updatedStudent.setDepartment(student.getDepartment());
        if(updatedStudent.getEducationLevel() == null)
            updatedStudent.setEducationLevel(student.getEducationLevel());
        if(updatedStudent.getAddress() == null)
            updatedStudent.setAddress(student.getAddress());
    }

    private static StudentDto transformtoStudentDto(Student student, ModelMapper modelMapper){
        return modelMapper.map(student, StudentDto.class);
    }

    private static Student transformtoStudent(StudentDto studentDto, ModelMapper modelMapper){
        return modelMapper.map(studentDto, Student.class);
    }

    public StudentDto updateProfileByUserId(StudentDto studentDto, String id) {
        Student student = repository.findByUserId(id);
        Student updatedStudent = transformtoStudent(studentDto, modelMapper);
        if(student == null){
            updatedStudent.setUserId(id);
        }
        else {
            updatedStudent.setId(student.getId());
            setAttributesIfNull(student, updatedStudent);
        }
        repository.save(updatedStudent);
        StudentDto updatedStudentDto = transformtoStudentDto(student, modelMapper);
        return updatedStudentDto;
    }

    public StudentDto getStudentByUserId(String id) {
        Student student = repository.findByUserId(id);
        StudentDto studentDto = transformtoStudentDto(student, modelMapper);
        return studentDto;
    }

    public String getPersonalEmailByUserId(String userId) {
        return repository.findPersonalEmailByUserId(userId).getPersonalEmail();
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


