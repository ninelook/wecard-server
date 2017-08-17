package com.ninelook.wecard.server.scene.message;

import com.ninelook.wecard.protocol.Communication;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 消息包装体
 * User: Ron
 */
public class NHttpMessage extends NMessage {

    public NHttpMessage(int uid, int mid, Communication.HeshReqMessage heshReqMessage, Channel channel) {
        super(uid, mid, heshReqMessage, channel);
    }

    /**
     * 写入数据到客户端
     *
     * @param messageResult
     */
    public void write(final MessageResult messageResult) {
        String json = messageResult.toJson();
        ByteBuf content = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        HttpHeaders.setContentLength(res, content.readableBytes());

        // Send the response
        ChannelFuture f = this.channel.writeAndFlush(res);
    }
}
