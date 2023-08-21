package com.example.chatroomPractice.utils;

import com.example.chatroomPractice.controller.WebSocketServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("jsonUtils")
public class JSONUtils<T> {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public String toJSONString(T object) {
        try {
            String string = mapper.writeValueAsString(object);
            return string;
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    public T fromJSONString(String str, Class<T> objClass) {
        try {
            T object = mapper.readValue(str, objClass);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
