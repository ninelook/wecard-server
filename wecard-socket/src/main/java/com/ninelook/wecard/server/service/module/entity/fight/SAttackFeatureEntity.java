package com.ninelook.wecard.server.service.module.entity.fight;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一招技能的攻击特征
 * User: Simon
 * Date: 14-1-6 下午10:07
 */
public class SAttackFeatureEntity {

    /**
     * 攻击力
     */
    private int attackNum = 0;

    /**
     * 攻击产生的效果
     */
    private List<Integer> effectList;

    /**
     * 攻击效果 - 暴击
     */
    public static final int EFFECT_ID_ATTACK_CRIT = 1;

    public int getAttackNum() {
        return attackNum;
    }

    public void setAttackNum(int attackNum) {
        this.attackNum = attackNum;
    }

    /**
     * 添加一个攻击效果
     * @param effectId
     */
    public void addEffect(int effectId) {
        if (effectList == null) {
            effectList = new ArrayList<Integer>();
        }

        effectList.add(effectId);
    }

    public List<Integer> getEffectList() {
        return effectList;
    }
}
