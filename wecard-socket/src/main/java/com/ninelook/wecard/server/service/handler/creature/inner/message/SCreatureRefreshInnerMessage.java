package com.ninelook.wecard.server.service.handler.creature.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.creature.inner.handler.SCreatureRefreshInnerHandler;

/**
 * 野怪刷新推送
 * User: Simon
 * Date: 14-1-16 下午11:18
 */
public class SCreatureRefreshInnerMessage extends SServiceInnerMessage implements MessageHaveHandler{

    public SCreatureRefreshInnerMessage() {
        super(0);
    }

    public void exeHandler() {
        new SCreatureRefreshInnerHandler().handle(this);
    }
}