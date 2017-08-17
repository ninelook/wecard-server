package com.ninelook.wecard.server.scene.message;

import com.ninelook.wecard.protocol.Communication;
import io.netty.channel.Channel;

/**
 * 消息包装体
 * User: Simon
 * Date: 13-12-25 PM8:56
 */
public class NMessage {
    protected long uid;

    private int mid = 0;

    private Communication.HeshReqMessage heshReqMessage;

    protected Channel channel;

    /**
     * 构建一个消息包装体
     *
     * @param uid               当前消息所属用户id
     * @param mid               消息ID
     * @param heshReqMessage    当前消息的原始protobuf消息
     * @param channel           当前消息的netty channel
     */
    public NMessage(long uid, int mid, Communication.HeshReqMessage heshReqMessage, Channel channel) {
        this.uid = uid;
        this.mid = mid;
        this.heshReqMessage = heshReqMessage;
        this.channel = channel;
    }

    /**
     * 构建一个消息包装体
     *
     * @param uid               当前消息所属用户id
     * @param mid               消息ID
     */
    public NMessage(long uid, int mid) {
        this.uid = uid;
        this.mid = mid;
    }

    /**
     * 构建一个消息包装体
     *
     * @param uid               当前消息所属用户id
     */
    public NMessage(long uid) {
        this.uid = uid;
    }

    /**
     * 返回当前消息的用户ID
     * @return
     */
    public long getUid() {
        return uid;
    }

    /**
     * 返回当前消息的消息ID
     * @return
     */
    public int getMid() {
        return mid;
    }

    /**
     * 返回当前消息关联的protobuf原始消息
     * @return
     */
    public Communication.HeshReqMessage getHeshReqMessage() {
        return heshReqMessage;
    }

    /**
     * 返回当前消息的nettyChannel
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

}
