package com.ninelook.wecard.server.service.handler.ticker.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.ticker.inner.handler.STickerNoticeInnerHandler;

/**
 * 滴答消息 - 通知
 * User: Simon
 * Date: 14-1-16 下午11:18
 */
public class STickerNoticeInnerMessage extends SServiceInnerMessage implements MessageHaveHandler{

    public STickerNoticeInnerMessage(int homeId) {
        super(0, homeId);
    }

    @Override
    public void exeHandler() {
        new STickerNoticeInnerHandler().handle(this);
    }
}