package com.example.springboot.thriveuniversitybackend.admin.repositories;

import com.example.springboot.thriveuniversitybackend.admin.models.PublicNotification;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicNotificationRepository extends MongoRepository<PublicNotification, String> {
    @Aggregation(pipeline = {
            "{'$sort' : {'createdOn' : -1}}", "{'$limit' : ?0}"
    })
    List<PublicNotification> findAllSortedByCreatedDateAndLimit(Long limit);
}
