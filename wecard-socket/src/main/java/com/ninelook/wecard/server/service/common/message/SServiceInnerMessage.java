package com.ninelook.wecard.server.service.common.message;

import com.ninelook.wecard.server.scene.message.NMessage;

/**
 * 服务层内部消息
 * User: Simon
 * Date: 14-1-16 下午10:39
 */
public class SServiceInnerMessage extends NMessage {
    private int homeId = 0;

    public SServiceInnerMessage(long uid, int homeId) {
        super(uid);
        this.homeId = homeId;
    }

    public SServiceInnerMessage(long uid) {
        super(uid);
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }
}