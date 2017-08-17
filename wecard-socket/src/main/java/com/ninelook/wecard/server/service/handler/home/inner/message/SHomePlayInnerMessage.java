package com.ninelook.wecard.server.service.handler.home.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.home.inner.handler.SHomePlayInnerHandler;

import java.util.List;

/**
 * 内部消息 - 进入房间
 * User: Simon
 * Date: 14-1-16 下午11:18
 */
public class SHomePlayInnerMessage extends SServiceInnerMessage implements MessageHaveHandler{
    private List<Long> joinPlayerList;

    /**
     * 选择的关卡副本ID
     */
    private int mapId;

    public SHomePlayInnerMessage(long uid, int homeId, List<Long> joinPlayerList) {
        super(uid, homeId);
        this.joinPlayerList = joinPlayerList;
    }

    public SHomePlayInnerMessage(long uid, int homeId, List<Long> joinPlayerList, int mapId) {
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

    @Override
    public void exeHandler() {
        new SHomePlayInnerHandler().handle(this);
    }
}