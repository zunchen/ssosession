package com.mela.nettynio.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 *      1.监听和接收进来的连接请求
 *      2.配置 Channel 来通知一个关于入站消息的 EchoServerHandler 实例
 *
 * 服务器的主代码组件是
 *       1.EchoServerHandler 实现了的业务逻辑
 *       2.在 main() 方法，引导了服务器
 *
 * 执行后者所需的步骤是：
 *      (1) 创建 ServerBootstrap 实例来引导服务器并随后绑定
 *      (2) 创建并分配一个 NioEventLoopGroup 实例来处理事件的处理，如接受新的连接和读/写数据。
 *      (3) 指定本地 InetSocketAddress 给服务器绑定
 *      (4) 通过 EchoServerHandler 实例给每一个新的 Channel 初始化
 *      (5) 最后调用 ServerBootstrap.bind() 绑定服务器
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);        //1 设置端口
        new EchoServer(port).start();                //2 引导/启动服务器,调用服务器的start()
    }

    public void start() throws Exception {
        // 3. 创建 EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)                                // 4. 创建 ServerBootstrap
                    .channel(NioServerSocketChannel.class)        // 5. 指定使用 NIO 的传输 Channel
                    .localAddress(new InetSocketAddress(port))    // 6. 设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoServerHandler());
                        }
                    });

            // 8. bind()绑定的服务器;sync 等待服务器socket关闭
            ChannelFuture future = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + future.channel().localAddress());

            // 优雅地关闭你的服务器
            future.channel().closeFuture().sync(); // 9. 关闭 channel 和块，直到它被关闭
        } finally {
            group.shutdownGracefully().sync(); // 10. 关机的 EventLoopGroup，释放所有资源
        }
    }

}
