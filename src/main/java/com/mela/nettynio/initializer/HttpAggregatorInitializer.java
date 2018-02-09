package com.mela.nettynio.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 *
 * 1. 实现HTTP消息自动聚合
 * Automatically aggregate HTTP message fragments
 *
 * HTTP请求、响应由许多部分组成，需要聚合他们形成完整的消息。
 *
 *
 * 2.HTTP的压缩和加压缩
 *  Netty支持gzip和deflate
 *
 *  Automatically compress HTTP messages
 *
 *  压缩与依赖
 *  注意，Java 6或者更早版本，如果要压缩数据，需要添加 jzlib 到 classpath
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        if (client) {
            // 1. client: 添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());

            // 2.1 client: 添加 HttpContentDecompressor 用于处理来自服务器的压缩的内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            // 2. server: 添加 HttpServerCodec 作为我们是 server 模式时
            pipeline.addLast("codec", new HttpServerCodec());

            // 2.2 server: HttpContentCompressor 用于压缩来自 client 支持的 HttpContentCompressor
            pipeline.addLast("compressor", new HttpContentCompressor());
        }

        // 3.添加 HttpObjectAggregator 到 ChannelPipeline, 使用最大消息值是 512kb
        pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
    }
}
