package com.ninelook.wecard.server.scene.dispatcher;

import com.ninelook.wecard.server.scene.message.NMessage;

/**
 * 派发器接口类
 * User: Simon
 * Date: 13-12-25 PM5:48
 */
public interface Dispatcher {
    /**
     * 派发消息
     */
    public void process(NMessage message) throws Exception;
}
