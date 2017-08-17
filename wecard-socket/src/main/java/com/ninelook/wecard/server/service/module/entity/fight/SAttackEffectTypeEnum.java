package com.ninelook.wecard.server.service.module.entity.fight;

/**
 * 攻击特效类型枚举类
 * User: Simon
 * Date: 14-1-6 下午10:07
 */
public enum SAttackEffectTypeEnum {
    /**
     * 攻击效果 - 暴击
     */
    TYPE_CRIT(1),

    /**
     * 攻击效果 - 闪避
     */
    TYPE_DODGE(2);

    private int index;

    private SAttackEffectTypeEnum(int index) {
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
