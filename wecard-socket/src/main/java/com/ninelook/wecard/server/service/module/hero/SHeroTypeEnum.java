package com.ninelook.wecard.server.service.module.hero;

/**
 *  英雄类型枚举
 */
public enum SHeroTypeEnum {

    /**
     * 类型 - 用户英雄
     */
    TYPE_HERO_USER(1),

    /**
     * 类型 - AI英雄
     */
    TYPE_HERO_AI(2);

    private int index;

    private SHeroTypeEnum(int index) {
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
