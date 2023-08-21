package com.example.chatroomPractice.controller;

public class CommonWsResponse {
    private String roomId;
    private String message;
    private String account;

    private String nickname;;

    public CommonWsResponse(String roomId, String message, String account, String nickname) {
        this.roomId = roomId;
        this.message = message;
        this.account = account;
        this.nickname = nickname;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getMessage() {
        return message;
    }

    public String getAccount() {
        return account;
    }

    public String getNickname() {
        return nickname;
    }
}
