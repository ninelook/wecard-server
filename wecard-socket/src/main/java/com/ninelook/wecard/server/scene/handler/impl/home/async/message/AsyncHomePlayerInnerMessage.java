package com.ninelook.wecard.server.scene.handler.impl.home.async.message;

import com.ninelook.wecard.server.scene.handler.impl.home.async.handler.AsyncHomePlayerInfoInnerHandler;
import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.scene.message.SceneInnerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 异步信息 - 获取用户信息
 * User: Simon
 * Date: 14-1-16 下午11:20
 */
public class AsyncHomePlayerInnerMessage extends SceneInnerMessage implements MessageHaveHandler {
    private int homeId;


    private List<Long> playerList = new ArrayList<Long>();

    public AsyncHomePlayerInnerMessage(long uid, int homeId) {
        super(uid);
        this.homeId = homeId;
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

    public void exeHandler() {
        new AsyncHomePlayerInfoInnerHandler().handle(this);
    }
}