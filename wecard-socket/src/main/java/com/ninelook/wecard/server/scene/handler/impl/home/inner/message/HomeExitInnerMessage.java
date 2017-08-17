package com.ninelook.wecard.server.scene.handler.impl.home.inner.message;

import com.ninelook.wecard.server.scene.handler.impl.home.inner.handler.HomeExitInnerHandler;
import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.scene.message.SceneInnerMessage;

/**
 * 场景内部消息 - 房间退出
 * User: Simon
 * Date: 14-1-16 下午11:20
 */
public class HomeExitInnerMessage extends SceneInnerMessage implements MessageHaveHandler {
    private int homeId;

    private boolean offline = false;

    public HomeExitInnerMessage(long uid, int homeId, boolean offline) {
        super(uid);
        this.homeId = homeId;
        this.offline = offline;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public int getHomeId() {
        return homeId;
    }

    public void exeHandler() {
        new HomeExitInnerHandler().handle(this);
    }
}