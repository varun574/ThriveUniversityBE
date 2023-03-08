package com.example.springboot.thriveuniversitybackend.task.repositories;

import com.example.springboot.thriveuniversitybackend.task.models.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
}
