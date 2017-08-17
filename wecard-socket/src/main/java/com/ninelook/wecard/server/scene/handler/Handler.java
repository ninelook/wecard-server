package com.ninelook.wecard.server.scene.handler;


import com.ninelook.wecard.server.scene.message.NMessage;

/**
 * 处理器接口类
 *
 * User: Simon
 * Date: 13-12-25 PM10:47
 */
public interface Handler {
    /**
     * 处理器核心入口
     * @param message
     */
    public void handle(NMessage message);
}
