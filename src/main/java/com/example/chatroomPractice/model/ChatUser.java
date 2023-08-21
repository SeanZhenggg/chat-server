package com.example.chatroomPractice.model;

import jakarta.websocket.Session;

public class ChatUser {
    private Session session;
    private String roomId;
    private String account;
    private String nickname;

    public ChatUser() {
    }

    public ChatUser(String roomId, String account, String nickname, Session session) {
        this.session = session;
        this.roomId = roomId;
        this.account = account;
        this.nickname = nickname;
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
