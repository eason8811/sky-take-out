package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("ws/{sid}")
@Slf4j
public class WebSocketServer {

    private final Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 建立连接成功时调用的方法
     *
     * @param session 连接的 session
     * @param sid 客户端的 sid
     */
    @OnOpen
    public void onOpen(Session session, @PathVariable String sid){
        log.info("客户端: {} 正在建立连接", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端: {} 的信息: {}", sid, message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid 客户端的 sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("连接断开: {}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 群发
     *
     * @param message 需要群发的消息
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
    }


}
