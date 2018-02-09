package com.mela.nettynio.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 *
 * 1. 支持HTTPS
 * 启用 HTTPS，只需添加 SslHandler
 *
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean client;

    public HttpsCodecInitializer(SslContext context, boolean client) {
        this.context = context;
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        SSLEngine engine = context.newEngine(ch.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine)); // 1. 添加 SslHandler 到 pipeline 来启用 HTTPS

        if (client) {
            // 2. client: 添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());

        } else {
            // 3. server: 添加 HttpServerCodec 作为我们是 server 模式时
            pipeline.addLast("codec", new HttpServerCodec());

        }
    }
}
