package com.example.chatroomPractice.service;

import com.example.chatroomPractice.dao.DaoResponse;
import com.example.chatroomPractice.dao.IUserRepository;
import com.example.chatroomPractice.model.LoginUser;
import com.example.chatroomPractice.model.User;
import com.example.chatroomPractice.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("userService")
public class UserService {
    private final IUserRepository userRepository;

    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public UserService(@Qualifier("userDao") IUserRepository userDao, JwtTokenUtils jwtTokenUtils) {
        this.userRepository = userDao;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public UserServiceResponse<User> addUser(User user) {
        UUID id = UUID.randomUUID();
        user.setId(id);
        DaoResponse<User> daoRes = this.userRepository.addUser(user);

        return new UserServiceResponse(daoRes.getCode(), daoRes.getMessage(), daoRes.getData());
    }

    public UserServiceResponse<User> getUser(String account) {
        DaoResponse<User> daoRes = this.userRepository.getUser(account);

        return new UserServiceResponse(daoRes.getCode(), daoRes.getMessage(), daoRes.getData());
    }

    public UserServiceResponse<List<User>> getUsers() {
        DaoResponse<List<User>> daoRes = this.userRepository.getUsers();

        return new UserServiceResponse(daoRes.getCode(), daoRes.getMessage(), daoRes.getData());
    }

    public UserServiceResponse<String> userLogin(LoginUser loginUser) {
        DaoResponse<User> daoRes = this.userRepository.userLogin(loginUser);

        if (daoRes.getData() != null) {
            User loggedIn = daoRes.getData();
            String token = this.jwtTokenUtils.generateToken(loggedIn);
            return new UserServiceResponse<>(daoRes.getCode(), "", token);
        }

        return new UserServiceResponse<>(4004, "Invalid user account.", null);
    }
}
