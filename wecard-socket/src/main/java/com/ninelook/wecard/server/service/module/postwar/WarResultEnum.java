package com.ninelook.wecard.server.service.module.postwar;

/**
 * 战斗结果枚举
 * User: Simon
 * Date: 4/22/14 11:35
 */
public enum WarResultEnum {
    /**
     * 战斗没有结束
     */
    RES_UNOVER(0),

    /**
     * 我方建筑损坏
     */
    RES_LEFT_BUILD(1),

    /**
     * 我方英雄全部死亡
     */
    RES_LEFT_HERO(2),

    /**
     * 敌方建筑损坏
     */
    RES_RIGHT_BUILD(3),

    /**
     * 敌方野怪全部死亡
     */
    RES_RIGHT_CREATURE(4),

    /**
     * 触发左侧胜利条件
     */
    RES_LEFT_WIN_TRIGGER(5),

    /**
     * 触发左侧失败条件
     */
    RES_LEFT_LOSE_TRIGGER(6);

    private int index;

    private WarResultEnum(int index) {
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
