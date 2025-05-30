package com.iot.server;

import com.iot.utils.WebSocketManageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;

@Slf4j
// 声明这是WebSocket服务器类，指定WebSocket的连接URL，{sid}是客户端传入的参数
// ws://host:port/websocket/{sid}
// host:port与HTTP相同
@ServerEndpoint("/websocket/{sid}")
// 虽然类被自动注册了，但别忘了交给Spring容器管理，否则不能生效
@Component
public class WebSocketServer {
    // 接收sid
    private String sid = "";
    // 当前WebSocket的会话对象
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("sid") String sid, Session session) {
        log.info("{} 连接 WebSocket 成功", sid);
        this.sid = sid;
        this.session = session;
        WebSocketManageUtils.addWebSocket(sid, this);
        try {
            // 通知客户端
            sendMessage("ok");
        } catch (IOException e) {
            log.error("WebSocket IO Exception");
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        log.info("{} 关闭连接", sid);
        WebSocketManageUtils.getWebSocket(sid).session.close();
        WebSocketManageUtils.removeWebSocket(sid);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // ...
    }

    /**
     * 通信异常时调用的方法
     */
    @OnError
    public void onError(Throwable error) {
        log.error("{} 连接发生错误", sid);
        error.printStackTrace();
    }

    /**
     * 服务器主动推送消息
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发
     */
    public void sendToAll(String message) throws IOException {
        log.info("WebSocket 群发：{}", message);
        Collection<WebSocketServer> allWebSocket = WebSocketManageUtils.getAllWebSocket();
        for (WebSocketServer webSocket : allWebSocket) {
            webSocket.sendMessage(message);
        }
    }
}
