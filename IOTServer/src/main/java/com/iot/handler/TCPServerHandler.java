package com.iot.handler;

import com.iot.service.HardwareService;
import com.iot.utils.SystemUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 继承自SimpleChannelInboundHandler<String>
 * 每当有客户端发来字符串消息时，channelRead0方法就会被调用，用来处理这些消息
 */
@Slf4j
@Component
/**
 * Netty默认要求每个连接的Channel都使用自己的handler实例，除非这个handler被标记为@Sharable
 * 否则，不能在多个Channel（即多个客户端连接）之间复用同一个handler实例
 * 确保handler共享后不会引起并发问题，不然建议使用默认方式
 * Component注解会把handler当成单例注入，而TCPServer类中把它重复加到了不同Channel中，Netty必然报错
 * 使用Component标注handler是因为这里自动注入了其它对象，因此handler也要改成自动注入，交给Spring管理
 * 由于TCPServerHandler类中只有HardwareService一个属性，并只是调用其中的方法
 * 而HardwareService的代码本身也不会出现并发问题，比如MP本身是线程安全的、WebSocket和TCP连接的集合都采用了ConcurrentHashMap
 * 所以，这里可以使用@Sharable
 */
@ChannelHandler.Sharable
public class TCPServerHandler extends SimpleChannelInboundHandler<String> {
    @Autowired
    private HardwareService hardwareService;

    /**
     * @param ctx 当前通道的上下文对象，可以用它发送数据、获取客户端信息等
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientAddress = SystemUtils.getClientAddress(ctx.channel());
        log.info("客户端 {} 连接成功", clientAddress);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientAddress = SystemUtils.getClientAddress(ctx.channel());
        log.info("客户端 {} 断开连接", clientAddress);
        super.channelInactive(ctx);
        // 掉线处理
        hardwareService.offline(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws IOException {
        String clientAddress = SystemUtils.getClientAddress(ctx.channel());
        log.info("{} 发来消息：{}", clientAddress, msg);
        hardwareService.parseMsg(msg, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("TCP 服务出现异常", cause);
        ctx.close();
    }
}
