package com.example.springboot.thriveuniversitybackend.task.repositories;

import com.example.springboot.thriveuniversitybackend.task.models.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    @Query(value = "{assignmentId : ?0}", fields = "{rollNo : 1}")
    List<Submission> findRollNosByAssignmentId(String assignmentId);

    @Query(value = "{assignmentId : ?0, rollNo : {$in : ?1}}", delete = true)
    void deleteAllByRollNo(String assignmentId, List<String> deleteRollNos);

    List<Submission> findAllByAssignmentId(String assignmentId);

    @Query(value = "{assignmentId : ?0}", delete = true)
    void deleteAllByAssignmentId(String assignmentId);

    @Query(value = "{assignmentId : ?0, rollNo : ?1}")
    Submission findByAssignmentIdAndRollNo(String assignmentId, String rollNo);
}
