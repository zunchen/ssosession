package com.mela.nettynio.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 一个 SslHandler 使用 ChannelInitializer 添加到 ChannelPipeline。
 * (回想一下,当 Channel 注册时 ChannelInitializer 用于设置 ChannelPipeline )
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean client;
    private final boolean startTls;

    // 1. 使用构造函数来传递 SSLContext 用于使用(startTls 是否启用)
    public SslChannelInitializer(SslContext context, boolean client, boolean startTls) {
        this.context = context;
        this.client = client;
        this.startTls = startTls;
    }

    protected void initChannel(Channel ch) throws Exception {
        // 2.从 SslContext 获得一个新的 SslEngine 。给每个 SslHandler 实例使用一个新的 SslEngine
        SSLEngine engine = context.newEngine(ch.alloc());

        // 3. 设置 SslEngine 是 client 或者是 server 模式
        engine.setUseClientMode(client);

        // 4. 添加 SslHandler 到 pipeline 作为第一个处理器
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}

/**
 * 在大多数情况下,SslHandler 将成为 ChannelPipeline 中的第一个 ChannelHandler 。
 * 这将确保所有其他 ChannelHandler 应用他们的逻辑到数据后加密后才发生,从而确保他们的变化是安全的。
 *
 * SslHandler 有很多有用的方法,如表8.1所示。
 * 例如,在握手阶段两端相互验证,商定一个加密方法。
 * 您可以配置 SslHandler 修改其行为或提供 在SSL/TLS 握手完成后发送通知,这样所有数据都将被加密。 SSL/TLS 握手将自动执行。
 */
