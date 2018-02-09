package com.mela.nettynio.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Echo Server
 * 将接受到的数据的拷贝发送给客户端
 *
 * 使用 ChannelHandler 的方式体现了关注点分离的设计原则；
 *
 */
@Sharable // 1. 标识这类的实例之间可以在 channel 里面共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端发现有客户端进入...");
    }

    /**
     * 每个信息入站都会调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8)); // 2. 打印消息
        ctx.write(in); // 3. 将所接收的消息返回给发送者。注意，这还没有冲刷数据
    }

    /**
     * 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) // 4. 冲刷所有待审消息到远程节点。关闭通道后，操作完成。
                .addListener(ChannelFutureListener.CLOSE);
        //注意服务端继承ChannelInboundHandlerAdapter（客户端SimpleChannelInboundHandler会小心的释放对 ByteBuf（保存信息） 的引用）：
        // write() 是异步的，在channelRead()返回时，可能还没有完成。这里ctxWriteAndFlush() 来释放信息【ByteBuf（保存信息） 的引用】
    }

    /**
     * 读操作时捕获到异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();                // 5. 处理堆栈异常
        ctx.close();                            // 6. 关闭通道
    }
}
