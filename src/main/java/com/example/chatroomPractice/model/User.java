package com.example.chatroomPractice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.rmi.server.UID;
import java.util.UUID;

@Document(collection = "user")
public class User {
    @Id
    private UUID id;

    private String account;
    private String password;
    private String nickname;

    public User(@JsonProperty("account") String account,
                @JsonProperty("password") String password,
                @JsonProperty("nickname") String nickname) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
