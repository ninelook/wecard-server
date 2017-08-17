package com.ninelook.wecard.server.scene.player;

/**
 * 玩家状态
 * User: Simon
 * Date: 13-12-17 PM9:47
 */
public enum PlayerStatusEnum {
    /**
     * 未初始
     */
    UNINIT(1),

    /**
     * 玩家已准备
     */
    READY(2),

    /**
     * 加载完毕
     */
    LOADED(3);

    private int index;

    private PlayerStatusEnum(int index) {
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
