package com.iot.server;

import com.iot.handler.TCPServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "tcp.server")
public class TCPServer implements CommandLineRunner {
    private Integer port;

    @Autowired
    private TCPServerHandler tcpServerHandler;

    // SpringBoot启动后会自动执行run方法，只执行一次
    @Override
    public void run(String... args) throws Exception {
        // 事件循环线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 负责接收客户端连接请求
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 负责处理已接入连接的数据读写
        try {
            // Netty的服务端启动类，用于配置整个TCP服务器
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 将前面创建的两个线程组设置进来，分别负责接收连接和处理连接
            bootstrap.group(bossGroup, workerGroup)
                    // 设置服务器使用的通道类型：非阻塞服务器端TCP通道
                    .channel(NioServerSocketChannel.class)
                    // 为每个新连接初始化处理逻辑，即“流水线”
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            // 处理链，按顺序处理读写数据
                            ChannelPipeline pipeline = channel.pipeline();
                            // 把字节数据转换为字符串
                            pipeline.addLast(new StringEncoder());
                            // 把字符串转换为字节数据
                            pipeline.addLast(new StringDecoder());
                            // 自定义处理器，用于处理收发逻辑
                            // 需要自动注入，因为TCPServerHandler里自动注入了其它对象，如果这里用new则会报错
                            pipeline.addLast(tcpServerHandler);
                        }
                    })
                    // 当连接过多时，服务器接受连接的队列最大值为128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 开启TCP keepalive机制，保持连接存活，避免长连接断掉
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动TCP服务并绑定到指定端口，sync()会阻塞当前线程，直到绑定完成
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("TCP 服务开启，监听端口：{}", port);
            // 等待服务器通道关闭，防止主线程提前退出
            future.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭线程组，释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
