package com.example.chatroomPractice.controller;

import com.example.chatroomPractice.model.LoginUser;
import com.example.chatroomPractice.model.User;
import com.example.chatroomPractice.service.UserService;
import com.example.chatroomPractice.service.UserServiceResponse;
import com.example.chatroomPractice.utils.JwtTokenUtils;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("api/user")
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    private static final int ONE_HOUR = 1 * 60 * 60;

    @Autowired
    public UserController(@Qualifier("userService") UserService userService, JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @PostMapping
    public ResponseEntity<CommonJsonResponse> addUser(@RequestBody User user) {
        UserServiceResponse<User> result = this.userService.addUser(user);

        if (result.getCode() != 0) {
            CommonJsonResponse errRes = new CommonJsonResponse(result.getCode(), result.getMessage(), null);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        CommonJsonResponse res = new CommonJsonResponse(0, "", null);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CommonJsonResponse> getUsers(@RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            String token = authorization.substring(7);
            this.jwtTokenUtils.validateToken(token);
        } catch (AuthException e) {
            CommonJsonResponse errRes = new CommonJsonResponse(4001, e.getMessage(), null);
            return new ResponseEntity<>(errRes, HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            CommonJsonResponse errRes = new CommonJsonResponse(4001, "authentication failed", null);
            return new ResponseEntity<>(errRes, HttpStatus.UNAUTHORIZED);
        }

        UserServiceResponse<List<User>> result = this.userService.getUsers();

        if (result.getCode() != 0) {
            CommonJsonResponse errRes = new CommonJsonResponse(result.getCode(), result.getMessage(), null);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        CommonJsonResponse res = new CommonJsonResponse(0, "", result.getData());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{account}")
    public ResponseEntity<CommonJsonResponse> getUser(@PathVariable("account") String account) {
        UserServiceResponse<User> result = this.userService.getUser(account);

        if (result.getCode() != 0) {
            CommonJsonResponse errRes = new CommonJsonResponse(result.getCode(), result.getMessage(), null);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        CommonJsonResponse res = new CommonJsonResponse(0, "", result.getData());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonJsonResponse> userLogin(@RequestBody LoginUser loginUser, HttpServletResponse response) {
        UserServiceResponse<String> result = this.userService.userLogin(loginUser);

        if (result.getCode() != 0) {
            CommonJsonResponse errRes = new CommonJsonResponse(result.getCode(), result.getMessage(), null);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        Cookie tokenCookie = new Cookie("token", result.getData());
        Cookie accountCookie = new Cookie("account", loginUser.getAccount());
        tokenCookie.setMaxAge(ONE_HOUR);
        tokenCookie.setPath("/");
        accountCookie.setMaxAge(ONE_HOUR);
        accountCookie.setPath("/");
        response.addCookie(tokenCookie);
        response.addCookie(accountCookie);

        CommonJsonResponse res = new CommonJsonResponse(0, "", result.getData());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
