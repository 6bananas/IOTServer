package com.iot.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManageUtils {
    private static final Map<String, Integer> clientMap = new ConcurrentHashMap<>();

    public static void addClient(String ip, Integer vid) {
        clientMap.put(ip, vid);
    }

    public static Integer getVid(String ip) {
        return clientMap.get(ip);
    }

    public static void removeClient(String ip) {
        clientMap.remove(ip);
    }
}
