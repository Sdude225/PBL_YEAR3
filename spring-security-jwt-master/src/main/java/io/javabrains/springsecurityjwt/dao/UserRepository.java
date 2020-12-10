package io.javabrains.springsecurityjwt.dao;


import io.javabrains.springsecurityjwt.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long>{
    void deleteUserByEmail(String email);
    Optional<User> findByEmail(String email);;
}
