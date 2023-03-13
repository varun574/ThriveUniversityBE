package com.example.springboot.thriveuniversitybackend.admin.services;

import com.example.springboot.thriveuniversitybackend.admin.dtos.SubjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private SubjectService subjectService;

    public List<SubjectDto> getAllSubjects(){
        return subjectService.getAllSubjects();
    }
    public void saveSubject(SubjectDto subjectDto){
        subjectService.saveSubject(subjectDto);
    }
    public void saveSubjects(List<SubjectDto> subjectDtos){
        subjectService.saveSubjects(subjectDtos);
    }
}
