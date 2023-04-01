package com.example.springboot.thriveuniversitybackend.Public.repositories;

import com.example.springboot.thriveuniversitybackend.Public.models.Admission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionRepository extends MongoRepository<Admission, String> {
    @Query(value = "{status : {$in : ?0}}")
    List<Admission> findAllAdmissionsByStatus(String[] status);
}
