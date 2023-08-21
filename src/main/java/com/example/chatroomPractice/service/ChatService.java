package com.example.chatroomPractice.service;

import com.example.chatroomPractice.model.ChatUser;
import com.example.chatroomPractice.model.User;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static final Map<String, Map<Session, ChatUser>> rooms = new ConcurrentHashMap<>();

    public static void sendMessageToAllUsers(String roomId, String message) {
        Map<Session, ChatUser> room = rooms.get(roomId);
        room.forEach((Session session, ChatUser chatUser) -> sendMessageToUser(chatUser.getSession(), message));
    }

    public synchronized static void sendMessageToUser(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error(String.format("sendMessageToUser error : %s", e.getMessage()));
        }
    }

    public static void addUser(String roomId, String account, String nickname, Session userSession) {
        if (roomId == null || roomId.isEmpty() || account == null || account.isEmpty() || nickname == null || nickname.isEmpty() || userSession == null)
            return;

        if (!rooms.containsKey(roomId)) rooms.put(roomId, new HashMap<>());

        Map<Session, ChatUser> map = rooms.get(roomId);

        if (!map.containsKey(userSession)) {
            map.put(userSession, new ChatUser(roomId, account, nickname, userSession));
        }
    }

    public static boolean removeUser(String roomId, Session userSession) {
        if (roomId == null || roomId.isEmpty() || userSession == null) return false;

        Map<Session, ChatUser> userMap = rooms.get(roomId);
        ChatUser chatUser = userMap.get(userSession);
        logger.info(String.format("removing user \"%s\" from room \"%s\"...", chatUser.getAccount(), chatUser.getRoomId()));
        ChatUser result = userMap.remove(userSession);

        if (result != null) {
            logger.info(String.format("user \"%s\" from room \"%s\" has been removed.", result.getAccount(), result.getRoomId()));
        }

        if (userMap.isEmpty()) rooms.remove(roomId);
        return true;
    }

    public static ChatUser getRoomAndUserBySession(Session userSession) {
        if (userSession == null) return null;

        ChatUser getUser = null;

        Set<Map.Entry<String, Map<Session, ChatUser>>> roomSet = rooms.entrySet();
        for (Map.Entry<String, Map<Session, ChatUser>> roomEntry : roomSet) {
            Map<Session, ChatUser> userMap = roomEntry.getValue();
            if (userMap.containsKey(userSession)) {
                getUser = userMap.get(userSession);
            }
        }

        return getUser;
    }
}
