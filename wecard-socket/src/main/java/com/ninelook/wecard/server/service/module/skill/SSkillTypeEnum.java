package com.ninelook.wecard.server.service.module.skill;

/**
 * 技能类型枚举类
 * User: Simon
 * Date: 3/3/14 16:59
 */
public enum SSkillTypeEnum {

    /**
     * 技能类型 - 攻击技能
     */
    SKILL_TYPE_ATTACK(0),

    /**
     * 技能类型 - 加血技能(type待定)
     */
    SKILL_TYPE_ADD_BLOOD(900000001),

    /**
     * 技能类型 - 召唤技能
     */
    SKILL_TYPE_CALLUP(6),

    /**
     * 技能类型 - 触发BUFF技能(type待定)
     */
    SKILL_TYPE_TOUCH_BUFF(2);

    private int index;

    private SSkillTypeEnum(int index) {
        this.index = index;
    }

    /**
     * 返回当前枚举的索引值
     * @return
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 通过索引返回一个枚举
     * @param index
     * @return
     */
    public static SSkillTypeEnum getEnumByIndex(int index) {
        for (SSkillTypeEnum e : SSkillTypeEnum.values()) {
            if (e.getIndex() == index)
                return e;
        }
        return null;
    }
}