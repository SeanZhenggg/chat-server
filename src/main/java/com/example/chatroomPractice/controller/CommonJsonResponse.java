package com.example.chatroomPractice.controller;

import java.util.HashMap;
import java.util.Map;

public class CommonJsonResponse {

    private int status;

    private String messages;

    private Object data;

    public CommonJsonResponse(int status, String messages, Object data) {
        this.status = status;
        this.messages = messages;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }

    public Object getData() {
        return data;
    }
}
