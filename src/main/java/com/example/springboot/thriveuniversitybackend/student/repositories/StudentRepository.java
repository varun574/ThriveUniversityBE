package com.example.springboot.thriveuniversitybackend.student.repositories;

import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import com.example.springboot.thriveuniversitybackend.student.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByRollNo(String rollNo);

    Student findByUserId(String id);

    @Query(value = "{'userId' : ?0 }", fields = "{'personalEmail':1}")
    Student findPersonalEmailByUserId(String userId);

    @Query(value = "{academicYear : ?0, department : ?1, section : ?2}", fields = "{rollNo : 1, _id : 0}")
    List<Student> findRollNosByAcademicDetails(AcademicYear academicYear, String department, String section);

    @Query(value = "{academicYear : ?0, department : {$in : ?1}}", fields = "{rollNo : 1}")
    List<Student> findRollNosByDepartment(AcademicYear academicYear, List<String> departments);

    @Query(value = "{rollNo : ?0}", count = true)
    Long findAllByRollNo(List<String> pendingRollNos);
}
