package com.ninelook.wecard.server.service.handler.home.async.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.scene.message.SceneInnerMessage;
import com.ninelook.wecard.server.service.handler.home.async.handler.AsyncHomeInitInnerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 异步信息 - 房间初始化
 * User: Simon
 * Date: 14-1-16 下午11:20
 */
public class AsyncHomeInitInnerMessage extends SceneInnerMessage implements MessageHaveHandler {
    private int homeId;

    private int mapId;

    private List<Long> playerList = new ArrayList<Long>();

    public AsyncHomeInitInnerMessage(long uid, int homeId, int mapId) {
        super(uid);
        this.homeId = homeId;
        this.mapId = mapId;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setPlayerList(List<Long> playerList) {
        this.playerList = playerList;
    }

    public List<Long> getPlayerList() {
        return playerList;
    }

    public int getMapId() {
        return mapId;
    }

    public void exeHandler() {
        new AsyncHomeInitInnerHandler().handle(this);
    }
}