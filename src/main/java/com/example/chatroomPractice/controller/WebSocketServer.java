package com.example.chatroomPractice.controller;

import com.example.chatroomPractice.model.ChatUser;
import com.example.chatroomPractice.model.User;
import com.example.chatroomPractice.service.ChatService;
import com.example.chatroomPractice.service.UserService;
import com.example.chatroomPractice.service.UserServiceResponse;
import com.example.chatroomPractice.utils.JSONUtils;
import com.example.chatroomPractice.utils.JwtTokenUtils;
import jakarta.security.auth.message.AuthException;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.bson.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static JwtTokenUtils jwtTokenUtils;

    private static JSONUtils<CommonWsResponse> jsonUtils;

    private static UserService userService;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws Exception {
        URI uri = session.getRequestURI();

        MultiValueMap<String, String> parameters =  UriComponentsBuilder.fromUriString(uri.toString()).build().getQueryParams();
        String account = parameters.getFirst("account");
        String token = parameters.getFirst("token");
        String roomId = parameters.getFirst("roomId");

        if (roomId.isEmpty() || account.isEmpty()) {
            session.close();
            return;
        }

        try {
            jwtTokenUtils.validateToken(token);
        } catch (AuthException e) {
            logger.info(e.getMessage());
            session.close();
            return;
        } catch (NullPointerException e) {
            logger.info(e.getMessage());
            session.close();
            return;
        }

        UserServiceResponse<User> user = userService.getUser(account);
        String nickname = user.getData().getNickname();
        ChatService.addUser(roomId, account, nickname, session);
        ChatService.sendMessageToAllUsers(roomId, jsonUtils.toJSONString(new CommonWsResponse(roomId, "已連線", account, nickname)));
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        ChatUser user = ChatService.getRoomAndUserBySession(session);
        if (user != null) {
            boolean result = ChatService.removeUser(user.getRoomId(), user.getSession());

            if (result) {
                logger.info(String.format("User \"%s\" from room \"%s\" closed the ws connection.", user.getAccount(), user.getRoomId()));
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        ChatUser user = ChatService.getRoomAndUserBySession(session);

        logger.info(String.format("receive message from user \"%s\" : %s", user.getAccount(), message));
        ChatService.sendMessageToAllUsers(user.getRoomId(), jsonUtils.toJSONString(new CommonWsResponse(user.getRoomId(), message, user.getAccount(), user.getNickname())));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info(throwable.toString());
        throwable.printStackTrace();
    }

    @Autowired
    public void setJwtTokenUtils(JwtTokenUtils jwtTokenUtils) {
        WebSocketServer.jwtTokenUtils = jwtTokenUtils;
    }

    @Autowired
    public void setJSONUtils(JSONUtils jsonUtils) {
        WebSocketServer.jsonUtils = jsonUtils;
    }

    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }
}
