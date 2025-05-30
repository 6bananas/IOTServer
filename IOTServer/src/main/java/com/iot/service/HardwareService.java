package com.iot.service;

import io.netty.channel.Channel;

import java.io.IOException;

/**
 * 处理硬件发送的消息
 */
public interface HardwareService {
    void parseMsg(String msg, Channel channel) throws IOException;
    void reset(Integer vid, Channel channel) throws IOException;

    void offline(Channel channel) throws IOException;
}
