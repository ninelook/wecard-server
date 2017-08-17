package com.ninelook.wecard.server.service.handler.home.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.home.inner.handler.SHomeInitInnerHandler;

import java.util.List;

/**
 * 内部消息 - 房间引导
 * User: Simon
 * Date: 14-1-16 下午11:18
 */
public class SHomeInitInnerMessage extends SServiceInnerMessage implements MessageHaveHandler{
    private List<Long> joinPlayerList;

    /**
     * 选择的关卡副本ID
     */
    private int mapId;

    public SHomeInitInnerMessage(long uid, int homeId, List<Long> joinPlayerList) {
        super(uid, homeId);
        this.joinPlayerList = joinPlayerList;
    }

    public SHomeInitInnerMessage(long uid, int homeId, List<Long> joinPlayerList, int mapId) {
        super(uid, homeId);
        this.joinPlayerList = joinPlayerList;
        this.mapId = mapId;
    }

    public List<Long> getJoinPlayerList() {
        return joinPlayerList;
    }

    public int getMapId() {
        return mapId;
    }

    public void exeHandler() {
        new SHomeInitInnerHandler().handle(this);
    }
}