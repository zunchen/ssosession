package com.mela.nettynio.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导客户端：
 *      客户端引导需要 host 、port 两个参数连接服务器
 *
 * 要点：
 *      （1）一个 Bootstrap 被创建来初始化客户端
 *      （2）一个 NioEventLoopGroup 实例被分配给处理该事件的处理，这包括创建新的连接和处理入站和出站数据
 *      （3）一个 InetSocketAddress 为连接到服务器而创建
 *      （4）一个 EchoClientHandler 将被安装在 pipeline 当连接完成时
 *      （5）之后 Bootstrap.connect（）被调用连接到远程的 - 本例就是 echo(回声)服务器
 *
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // NioEventLoopGroup用来处理I/O操作的多线程事件循环器
        try {
            Bootstrap bootstrap = new Bootstrap();                // 1. 创建 Bootstrap,初始化客户端
            bootstrap.group(group)                                // 2. 指定 EventLoopGroup 来处理客户端事件。由于我们使用 NIO 传输，所以用到了 NioEventLoopGroup 的实现
                    .channel(NioSocketChannel.class)            // 3. 使用的 channel 类型是一个用于 NIO 传输(一个新的 Channel 接收进来的连接)
                    .remoteAddress(new InetSocketAddress(host, port))    // 4. 设置服务器的 InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() {    // 5. 当建立一个连接和一个新的通道时，创建添加EchoClientHandler 实例 到 channel pipeline
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();        // 6. connect()连接到远程;sync等待连接完成

            future.channel().closeFuture().sync();            // 7. 阻塞直到 Channel 关闭
        } finally {
            group.shutdownGracefully().sync();            // 8. 调用 shutdownGracefully() 来关闭线程池和释放所有资源
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() +
                            " <host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        new EchoClient(host, port).start();
    }
}
