package com.ninelook.wecard.server.gateway.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @author Ron
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 读操作空闲30秒
     */
    private final static int readerIdleTimeSeconds = 10;

    /**
     * 写操作空闲60秒
     */
    private final static int writerIdleTimeSeconds = 0;

    /**
     * 读写全部空闲100秒
     */
    private final static int allIdleTimeSeconds = 0;

    private final SslContext sslCtx;

    public NettyServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));

//        pipeline.addLast(new ProtobufVarint32FrameDecoder());
//        pipeline.addLast(new ProtobufDecoder(Communication.HeshReqMessage.getDefaultInstance()));
//
//        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
//        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new NettyServerHandler());




    }
}
