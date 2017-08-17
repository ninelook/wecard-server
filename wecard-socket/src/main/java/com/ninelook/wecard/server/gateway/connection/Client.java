package com.ninelook.wecard.server.gateway.connection;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 连接的客户端
 * User: Simon
 * Date: 13-12-13 PM2:36
 */
public class Client {

    /**
     * 当前客户端关联的通道
     */
    private Channel channel;

    /**
     * 当前客户端用户ID
     */
    private Long uid;

    public Client(Long uid, Channel channel) {
        this.uid = uid;
        this.channel = channel;
    }

    /**
     * 获取当前客户客户端的channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * 获取用户ID
     * @return
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 向当前客户端发送数据
     * @param message
     */
    public void send(Message message) {
        byte[] bytes = message.toByteArray();

        ByteBuf b = Unpooled.buffer(bytes.length);
        b.writeBytes(bytes);

        WebSocketFrame frame = new BinaryWebSocketFrame(b);

        channel.writeAndFlush(frame);
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        channel.close();
    }

}
