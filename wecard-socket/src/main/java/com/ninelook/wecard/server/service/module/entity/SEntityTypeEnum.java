package com.ninelook.wecard.server.service.module.entity;

/**
 * 实体类型
 * User: Simon
 * Date: 2/12/14 15:53
 */
public enum SEntityTypeEnum {

    /**
     * 类型 - 建筑
     */
    TYPE_BUILD(1),

    /**
     * 类型 - 英雄
     */
    TYPE_HERO(2),

    /**
     * 类型 - 野怪
     */
    TYPE_CREATURE(3);

    private int index;

    private SEntityTypeEnum(int index) {
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
