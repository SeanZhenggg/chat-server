package com.example.chatroomPractice.dao;

import com.example.chatroomPractice.controller.WebSocketServer;
import com.example.chatroomPractice.model.LoginUser;
import com.example.chatroomPractice.model.User;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDao")
public class UserRepository implements IUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final MongoUserRepository mongoUserRepository;

    @Autowired
    public UserRepository(MongoUserRepository mongoUserRepository) {
        this.mongoUserRepository = mongoUserRepository;
    }

    @Override
    public DaoResponse<User> addUser(User user) {
        try {
            this.mongoUserRepository.insert(user);
            return new DaoResponse(0, "", null);
        } catch (DuplicateKeyException e) {
            logger.error(String.format("[DuplicateKeyException] : %s", e.getMessage()));
            return new DaoResponse(11000, e.getMessage(), null);
        } catch (MongoWriteException e) {
            logger.error(String.format("[MongoWriteException] : %s", e.getMessage()));
            return new DaoResponse(e.getCode(), e.getMessage(), null);
        }
    }

    @Override
    public DaoResponse<User> getUser(String account) {
        try {
            User user = this.mongoUserRepository.findByAccount(account);
            return new DaoResponse(0, "", user);
        } catch (MongoException e) {
            logger.error(String.format("[MongoException] : %s", e.getMessage()));
            return new DaoResponse(e.getCode(), e.getMessage(), null);
        }
    }

    @Override
    public DaoResponse<List<User>> getUsers() {
        try {
            List<User> users = this.mongoUserRepository.findAll();
            return new DaoResponse(0, "", users);
        } catch (MongoException e) {
            logger.error(String.format("[MongoException] : %s", e.getMessage()));
            return new DaoResponse(e.getCode(), e.getMessage(), null);
        }
    }

    @Override
    public DaoResponse<User> userLogin(LoginUser loginUser) {
        try {
            User user = this.mongoUserRepository.findByAccountAndPassword(
                    loginUser.getAccount(),
                    loginUser.getPassword()
            );
            return new DaoResponse(0, "", user);
        } catch (MongoException e) {
            logger.error(String.format("[MongoException] : %s", e.getMessage()));
            return new DaoResponse(e.getCode(), e.getMessage(), null);
        }
    }
}