package com.iot.utils;

import com.iot.server.WebSocketServer;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerEndpoint注解会为每个连接创建一个WebSocketServer实例
 * 定义Map来管理所有WebSocket连接
 */
public class WebSocketManageUtils {
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    public static void addWebSocket(String sid, WebSocketServer webSocketServer) {
        webSocketMap.put(sid, webSocketServer);
    }

    public static WebSocketServer getWebSocket(String sid) {
        return webSocketMap.get(sid);
    }

    public static void removeWebSocket(String sid) {
        webSocketMap.remove(sid);
    }

    public static Collection<WebSocketServer> getAllWebSocket() {
        return webSocketMap.values();
    }
}
