package com.ninelook.wecard.server.scene.home;

/**
 * 房间状态枚举类
 * User: Simon
 * Date: 13-12-17 PM9:21
 */
public enum HomeStatusEnum {
    /**
     * 未初始
     */
    UNINIT(1),

    /**
     * 玩家准备中
     */
    PLAYER_READY_DOING(2),

    /**
     * 加载中
     */
    DATA_LOADING(3),

    /**
     * 加载完成
     */
    DATA_LOADED(4),

    /**
     * 客户端资源加载完成
     */
    CLIENT_RESOURCE_LOADED(5),

    /**
     * 战斗中
     */
    FIGHT_DOING(6);

    private int index;

    private HomeStatusEnum(int index) {
        this.index = index;
    }

    /**
     * 返回当前枚举的索引值
     * @return
     */
    public int getIndex() {
        return this.index;
    }
}
