package com.example.springboot.thriveuniversitybackend.student.repositories;

import com.example.springboot.thriveuniversitybackend.student.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByRollNo(String rollNo);

    Student findByUserId(String id);

    @Query(value = "{'userId' : ?0 }", fields = "{'personalEmail':1}")
    Student findPersonalEmailByUserId(String userId);
}
