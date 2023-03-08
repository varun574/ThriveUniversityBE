package com.example.springboot.thriveuniversitybackend.task.repositories;

import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.task.models.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    @Query("{$and : [{academicYear : ?1}, { $or : [{assignedTo : ?0}, {assignedTo : ?2}, {assignedTo : ?3}]}]}")
    List<Assignment> findAllByStudentDetails(String userId, AcademicYear academicYear, String department, String className);

    List<Assignment> findByCreatedBy(String userId);
}
