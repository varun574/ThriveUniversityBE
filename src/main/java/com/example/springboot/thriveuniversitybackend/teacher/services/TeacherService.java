package com.example.springboot.thriveuniversitybackend.teacher.services;

import com.example.springboot.thriveuniversitybackend.firebase.FileService;
import com.example.springboot.thriveuniversitybackend.teacher.dtos.TeacherDto;
import com.example.springboot.thriveuniversitybackend.teacher.models.Teacher;
import com.example.springboot.thriveuniversitybackend.teacher.repositories.TeacherRepository;
import jakarta.validation.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    private static void setAttributesIfNull(Teacher teacher, Teacher updatedTeacher) {
        if(updatedTeacher.getPersonalEmail() == null)
            updatedTeacher.setPersonalEmail(teacher.getPersonalEmail());
        if(updatedTeacher.getMobileNumber() == null)
            updatedTeacher.setMobileNumber(teacher.getMobileNumber());
        if(updatedTeacher.getDepartment() == null)
            updatedTeacher.setDepartment(teacher.getDepartment());
        if(updatedTeacher.getAddress() == null)
            updatedTeacher.setAddress(teacher.getAddress());
    }
    private static Teacher transformToTeacher(TeacherDto teacherDto, ModelMapper modelMapper) {
        return modelMapper.map(teacherDto, Teacher.class);
    }
    private static TeacherDto transformToTeacherDto(Teacher savedTeacher, ModelMapper modelMapper) {
        return modelMapper.map(savedTeacher, TeacherDto.class);
    }

    private void validate(Teacher teacher) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public TeacherDto updateProfileByUserId(TeacherDto teacherDto, String id) {
        Teacher teacher = repository.findByUserId(id);
        Teacher updatedTeacher = transformToTeacher(teacherDto, modelMapper);
        if(teacher == null){
            updatedTeacher.setUserId(id);
        }
        else {
            updatedTeacher.setId(teacher.getId());
            setAttributesIfNull(teacher, updatedTeacher);
        }
        repository.save(updatedTeacher);
        TeacherDto updatedTeacherDto = transformToTeacherDto(updatedTeacher, modelMapper);
        return updatedTeacherDto;
    }

    public TeacherDto getTeacherByUserId(String id) {
        Teacher teacher = repository.findByUserId(id);
        TeacherDto teacherDto = transformToTeacherDto(teacher, modelMapper);
        return teacherDto;
    }

    public String getPersonalEmailByUserId(String userId) {
        return repository.findPersonalEmailByUserId(userId).getPersonalEmail();
    }
}
