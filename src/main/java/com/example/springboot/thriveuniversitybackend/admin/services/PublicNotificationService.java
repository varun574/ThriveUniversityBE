package com.example.springboot.thriveuniversitybackend.admin.services;

import com.example.springboot.thriveuniversitybackend.admin.dtos.PublicNotificationDto;
import com.example.springboot.thriveuniversitybackend.admin.models.PublicNotification;
import com.example.springboot.thriveuniversitybackend.admin.repositories.PublicNotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicNotificationService {
    @Autowired
    private PublicNotificationRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public void postNotification(PublicNotificationDto publicNotificationDto){
        PublicNotification publicNotification = modelMapper.map(publicNotificationDto, PublicNotification.class);
        publicNotification.setCreatedOn(LocalDateTime.now());
        repository.save(publicNotification);
    }

    public List<PublicNotificationDto> getLatestNotifications(Long limit){
        List<PublicNotification> notifications = repository.findAllSortedByCreatedDateAndLimit(limit);
        List<PublicNotificationDto> latestNotifications = new ArrayList<>();
        for (PublicNotification notification: notifications
             ) {
            latestNotifications.add(modelMapper.map(notification, PublicNotificationDto.class));
        }
        return latestNotifications;
    }
}
