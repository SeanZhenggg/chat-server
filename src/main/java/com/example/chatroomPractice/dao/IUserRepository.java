package com.example.chatroomPractice.dao;

import com.example.chatroomPractice.model.LoginUser;
import com.example.chatroomPractice.model.User;

import java.util.List;

public interface IUserRepository {
    DaoResponse addUser(User user);

    DaoResponse<User> getUser(String account);

    DaoResponse<List<User>> getUsers();

    DaoResponse<User> userLogin(LoginUser loginUser);
}