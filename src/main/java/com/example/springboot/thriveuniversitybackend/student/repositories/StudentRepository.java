package com.example.springboot.thriveuniversitybackend.student.repositories;

import com.example.springboot.thriveuniversitybackend.student.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    Student findByEmail(String email);
    Student findByRollNo(String rollNo);
}
