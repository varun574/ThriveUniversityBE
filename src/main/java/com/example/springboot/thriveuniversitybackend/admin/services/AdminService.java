package com.example.springboot.thriveuniversitybackend.admin.services;

import com.example.springboot.thriveuniversitybackend.Public.models.Admission;
import com.example.springboot.thriveuniversitybackend.Public.repositories.AdmissionRepository;
import com.example.springboot.thriveuniversitybackend.admin.dtos.PublicNotificationDto;
import com.example.springboot.thriveuniversitybackend.admin.dtos.SubjectDto;
import com.example.springboot.thriveuniversitybackend.admin.exceptions.AdmissionNotFoundException;
import com.example.springboot.thriveuniversitybackend.enums.AdmissionStatus;
import com.example.springboot.thriveuniversitybackend.validators.annotations.EnumValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private PublicNotificationService publicNotificationService;

    public List<SubjectDto> getAllSubjects(){
        return subjectService.getAllSubjects();
    }
    public void saveSubject(SubjectDto subjectDto){
        subjectService.saveSubject(subjectDto);
    }
    public void saveSubjects(List<SubjectDto> subjectDtos){
        subjectService.saveSubjects(subjectDtos);
    }

    public void saveNotification(PublicNotificationDto publicNotificationDto){
        publicNotificationService.postNotification(publicNotificationDto);
    }

    public List<PublicNotificationDto> getLatestNotifications(Long limit){
        return publicNotificationService.getLatestNotifications(limit);
    }

    public void updateAdmissionStatus(String id, String status) {
        Optional<Admission> result = admissionRepository.findById(id);
        result.orElseThrow(() -> new AdmissionNotFoundException("Requested admission application is not found"));
        Admission admission = result.get();
        admission.setStatus(status);
        admissionRepository.save(admission);
    }

    public List<Admission> getAllAdmissions(List<String> status) {
        if(status == null || status.isEmpty()){
            return admissionRepository.findAll();
        }
        return admissionRepository.findAllAdmissionsByStatus(status.toArray(String[]::new));
    }
}
