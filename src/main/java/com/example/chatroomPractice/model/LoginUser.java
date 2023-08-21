package com.example.chatroomPractice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginUser {

    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public LoginUser(@JsonProperty("account") String account,
                     @JsonProperty("password") String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
