package com.iot.utils;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 前端只知道VID不知道对应的IP，后端只知道IP，只有发送了复位消息才知道IP与VID的对应
 * 维护一个channelMap，key是VID，value是channel，用于前端消息转发给硬件
 * 维护一个clientMap，key是IP，value是VID，用户维护在线硬件
 */
public class ChannelManageUtils {
    private static final Map<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    public static void addChannel(Integer vid, Channel channel) {
        channelMap.put(vid, channel);
    }

    public static Channel getChannel(Integer vid) {
        return channelMap.get(vid);
    }

    public static void removeChannel(Integer vid) {
        channelMap.remove(vid);
    }

    public static boolean sendMessage(Integer vid, String message) {
        Channel channel = channelMap.get(vid);
        if (channel != null) {
            channel.writeAndFlush(message);
            return true;
        }
        return false;
    }
}
