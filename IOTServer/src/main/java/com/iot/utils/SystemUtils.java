package com.iot.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class SystemUtils {
    public static String getClientAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        String port = socketAddress.getPort() + "";
        return ip + ":" + port;
    }
}
