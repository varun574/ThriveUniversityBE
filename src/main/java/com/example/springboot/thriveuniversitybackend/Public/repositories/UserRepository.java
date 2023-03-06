package com.example.springboot.thriveuniversitybackend.Public.repositories;

import com.example.springboot.thriveuniversitybackend.Public.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

}
