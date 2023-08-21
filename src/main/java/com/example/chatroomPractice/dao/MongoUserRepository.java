package com.example.chatroomPractice.dao;

import com.example.chatroomPractice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoUserRepository extends MongoRepository<User, UUID> {
    User findByAccount(String account);

    User findByAccountAndPassword(String account, String password);
}
