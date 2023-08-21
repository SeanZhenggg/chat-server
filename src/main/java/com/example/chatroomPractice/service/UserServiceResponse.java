package com.example.chatroomPractice.service;

public class UserServiceResponse<T> {
    private int code;
    private T data;

    private String message;

    public UserServiceResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
