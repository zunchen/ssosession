package com.mela.nettynio.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端要做的是：
 *    (1) 连接服务器
 *    (2) 发送信息
 *    (3) 发送的每个信息，等待和接收从服务器返回的同样的信息
 *    (4) 关闭连接
 *
 * 关于Netty：Netty提供 ChannelInboundHandler 来处理数据。
 *      下面例子，我们用 SimpleChannelInboundHandler 来处理所有的任务，需要覆盖三个方法：
 *      (1) channelActive() - 服务器的连接被建立后调用
 *      (2) channelRead0() - 数据后从服务器接收到调用
 *      (3) exceptionCaught() - 捕获一个异常时调用
 *
 * SimpleChannelInboundHandler vs. ChannelInboundHandler
 *      何时用这2个要看具体业务的需要。
 *      (1) 在客户端EchoClientHandler:
 *      当 channelRead0() 完成，我们已经拿到的入站的信息。
 *      当方法返回，SimpleChannelInboundHandler 会小心的释放对 ByteBuf（保存信息） 的引用。
 *      (2) 而在服务端EchoServerHandler：
 *      我们需要将入站的信息返回给发送者，write() 是异步的，在 channelRead()返回时，可能还没有完成。
 *      所以，我们使用 ChannelInboundHandlerAdapter,无需释放信息。
 *      最后在 channelReadComplete() 我们调用 ctxWriteAndFlush() 来释放信息【ByteBuf（保存信息） 的引用】。
 *
 */
@Sharable // 1. 标记这个类的实例可以在 channel 里共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 建立连接后该 channelActive() 方法被调用一次.
     * 逻辑很简单：一旦建立了连接，字节序列被发送到服务器。
     *      该消息的内容并不重要;
     *      在这里，我们使用了 Netty 编码字符串 “Netty rocks!”
     *      通过覆盖该方法，我们确保东西被尽快写入到服务器。
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 2. 当被通知该 channel 是活动的时候,就发送信息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                CharsetUtil.UTF_8));
    }

    /**
     * 方法会在接收到数据时被调用
     *
     * 注意，由服务器所发送的消息可以以块的形式被接收。
     *      即: 当服务器发送 5 个字节是不是保证所有的 5 个字节会立刻收到?
     *          即使是只有 5 个字节，channelRead0() 方法可被调用两次，
     *          第一次用一个ByteBuf（Netty的字节容器）装载3个字节和第二次一个 ByteBuf 装载 2 个字节。
     *          唯一要保证的是，该字节将按照它们发送的顺序分别被接收。 （注意，这是真实的，只有面向流的协议如TCP）。
     *
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 3. 记录接收到的消息
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        // 4. 记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
