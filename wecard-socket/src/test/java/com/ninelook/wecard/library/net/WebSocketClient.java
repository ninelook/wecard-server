/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ninelook.wecard.library.net;

import com.ninelook.wecard.library.common.PushInfoReceiver;
import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.protocol.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

/**
 * This is an example of a WebSocket client.
 * <p>
 * In order to run this example you need a compatible WebSocket server.
 * Therefore you can either start the WebSocket server from the examples
 * or connect to an existing WebSocket server such as
 * <a href="http://www.websocket.org/echo.html">ws://echo.websocket.org</a>.
 * <p>
 * The client will attempt to connect to the URI passed to it as the first argument.
 * You don't have to specify any arguments if you want to connect to the example WebSocket server,
 * as this is the default.
 */
public final class WebSocketClient {

    protected String url;

    protected long uid;

    private Channel channel;

    protected WebSocketClientHandler handler;

    public WebSocketClient(long uid, String url) {
        this.uid = uid;
        this.url = url;
    }


    public void run() throws Exception {
        URI uri = new URI(url);

        String scheme = uri.getScheme() == null? "ws" : uri.getScheme();
        final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported.");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            this.handler = new WebSocketClientHandler(uid,
                    WebSocketClientHandshakerFactory.newHandshaker(
                            uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }

                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192)
                            );
//
//                            p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//                            p.addLast("protobufDecoder", new ProtobufDecoder(Response.HeshResMessage.getDefaultInstance()));
//
//                            p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//                            p.addLast("protobufEncoder", new ProtobufEncoder());

                            p.addLast(handler);

                        }
                    });

            this.channel = b.connect(uri.getHost(), port).sync().channel();
//            handler.handshakeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
    }

    public ChannelFuture write(Communication.HeshReqMessage message) {
        byte[] bytes = message.toByteArray();

        ByteBuf b = Unpooled.buffer(bytes.length);
        b.writeBytes(bytes);

        WebSocketFrame frame = new BinaryWebSocketFrame(b);
        return channel.writeAndFlush(frame);
    }

    /**
     * 在信息接收队列内读取一条信息
     *
     * @return
     */
    public Response.HeshResMessage read() {
        Response.HeshResMessage msg = null;
        try {
            do{
                msg = this.handler.resQueue.take();
                if (msg.getPush() == true) {
                    PushInfoReceiver.getInstance().addPushInfo(this.uid, msg);
                }
            } while (msg.getPush() == true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }


    public void close() {
        this.channel.close();
    }
}
