package com.example.springboot.thriveuniversitybackend.admin.services;


import com.example.springboot.thriveuniversitybackend.Public.exceptions.UniqueConstraintException;
import com.example.springboot.thriveuniversitybackend.admin.dtos.SubjectDto;
import com.example.springboot.thriveuniversitybackend.admin.models.Subject;
import com.example.springboot.thriveuniversitybackend.admin.repositories.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    private static Subject transformToSubject(SubjectDto subjectDto, ModelMapper modelMapper) {
        return modelMapper.map(subjectDto, Subject.class);
    }
    private static SubjectDto transformToSubjectDto(Subject subject, ModelMapper modelMapper) {
        return modelMapper.map(subject, SubjectDto.class);
    }
    public List<SubjectDto> getAllSubjects(){
        List<SubjectDto> subjectDtos = repository.findAll().stream().map(subject -> transformToSubjectDto(subject, modelMapper)).collect(Collectors.toList());
        return subjectDtos;
    }
    public void saveSubject(SubjectDto subjectDto){
        Subject subject = transformToSubject(subjectDto, modelMapper);
        subject.setSubjectName(subject.getSubjectName().toLowerCase());
        repository.save(subject);
    }
    public void saveSubjects(List<SubjectDto> subjectDtos){
        List<Subject> subjects = subjectDtos.stream().map(subjectDto -> transformToSubject(subjectDto, modelMapper)).peek(subject -> subject.setSubjectName(subject.getSubjectName().toLowerCase())).toList();
        try{
            repository.saveAll(subjects);
        }
        catch (DuplicateKeyException exception){
            throw new UniqueConstraintException("Value Already exists", "subjectName");
        }
    }
}
