package com.streamLit.AuthService.authService.repo;

import com.streamLit.AuthService.authService.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> findByUserName(String username);
}
