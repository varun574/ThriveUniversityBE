package com.example.springboot.thriveuniversitybackend.teacher.repositories;

import com.example.springboot.thriveuniversitybackend.teacher.models.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Teacher findByUserId(String id);
    @Query(value = "{'userId' : ?0 }", fields = "{'personalEmail':1}")
    Teacher findPersonalEmailByUserId(String userId);
}
