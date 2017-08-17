package com.ninelook.wecard.server.scene.message;

/**
 * 消息具有handler指向方法
 * User: Simon
 * Date: 14-1-16 下午11:41
 */
public interface MessageHaveHandler {
    /**
     * 执行当前消息的handler方法
     * @return
     */
    public void exeHandler();
}
