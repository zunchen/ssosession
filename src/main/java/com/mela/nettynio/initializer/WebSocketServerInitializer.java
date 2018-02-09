package com.mela.nettynio.initializer;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 添加应用程序支持 WebSocket 只需要添加适当的客户端或服务器端WebSocket ChannelHandler 到管道。
 * 这个类将处理特殊 WebSocket 定义的消息类型,称为“帧。“如表8.3所示,这些可以归类为“数据”和“控制”帧。
 *
 *
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),  //1 添加 HttpObjectAggregator 用于提供在握手时聚合 HttpRequest
                // 2. 添加 WebSocketServerProtocolHandler 用于处理色好给你寄握手如果请求是发送到"/websocket." 端点，
                // 当升级完成后，它将会处理Ping, Pong 和 Close 帧
                new WebSocketServerProtocolHandler("/websocket"),
                new TextFrameHandler(),  //3 TextFrameHandler 将会处理 TextWebSocketFrames
                new BinaryFrameHandler(),  //4 BinaryFrameHandler 将会处理 BinaryWebSocketFrames
                new ContinuationFrameHandler());//5 ContinuationFrameHandler 将会处理ContinuationWebSocketFrames

//        加密 WebSocket 只需插入 SslHandler 到作为 pipline 第一个 ChannelHandler
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            // Handle text frame
        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
            // Handle binary frame
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
            // Handle continuation frame
        }
    }
}
