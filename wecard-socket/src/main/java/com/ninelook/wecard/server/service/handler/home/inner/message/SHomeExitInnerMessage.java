package com.ninelook.wecard.server.service.handler.home.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.home.inner.handler.SHomeExitInnerHandler;

/**
 * 内部类消息
 * User: Simon
 * Date: 14-1-16 下午11:16
 */
public class SHomeExitInnerMessage extends SServiceInnerMessage implements MessageHaveHandler {
    //是否为掉线
    private boolean offline = false;

    public SHomeExitInnerMessage(long uid, int homeId, boolean offline) {
        super(uid, homeId);
        this.offline = offline;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @Override
    public void exeHandler() {
        new SHomeExitInnerHandler().handle(this);
    }
}
