package com.ninelook.wecard.server.service.handler.fight.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.fight.inner.handler.SDeadInnerHandler;

/*
 * 死亡消息
 * User: Simon
 * Date: 13-12-25 PM8:56
 */
public class SDeadInnerMessage extends SServiceInnerMessage implements MessageHaveHandler {
    private int eid;

    public SDeadInnerMessage(int eid) {
        super(0);
        this.eid = eid;
    }

    public int getEid() {
        return eid;
    }

    @Override
    public void exeHandler() {
        new SDeadInnerHandler().handle(this);
    }
}