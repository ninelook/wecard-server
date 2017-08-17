package com.ninelook.wecard.server.scene.player;

/**
 * 登陆的玩家
 * User: Simon
 * Date: 13-12-17 PM9:13
 */
public class Player {

    /**
     * 用户ID
     */
    private long uid;

    /**
     * 房间ID
     */
    private int homeId;

    /**
     * 用户状态
     */
    private PlayerStatusEnum status = PlayerStatusEnum.UNINIT;

    public Player(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public PlayerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PlayerStatusEnum status) {
        this.status = status;
    }
}
