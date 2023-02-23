package com.example.springboot.thriveuniversitybackend.student.services;

import com.example.springboot.thriveuniversitybackend.common.LoginDto;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import com.example.springboot.thriveuniversitybackend.student.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.springboot.thriveuniversitybackend.utils.RandomStringGenerator.generateRandomString;

@Service
@Transactional
@Slf4j
public class StudentService {

    @Autowired
    private StudentRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    public StudentDto save(@Valid StudentDto studentdto) {
        Student student = toStudent(studentdto);
        student.setPassword(generateRandomString(48, 122, 8));
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

    public StudentDto toStudentDto(Student student){
        return modelMapper.map(student, StudentDto.class);
    }

    public Student toStudent(StudentDto studentDto){
        return modelMapper.map(studentDto, Student.class);
    }
}


