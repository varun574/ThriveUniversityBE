package com.example.springboot.thriveuniversitybackend.admin.repositories;

import com.example.springboot.thriveuniversitybackend.admin.models.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    boolean existsBySubjectName(String subjectName);
}
