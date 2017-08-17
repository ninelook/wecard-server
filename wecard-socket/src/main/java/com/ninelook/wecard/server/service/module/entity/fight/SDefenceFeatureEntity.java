package com.ninelook.wecard.server.service.module.entity.fight;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一招技能的防御特征
 * User: Simon
 * Date: 14-1-6 下午10:07
 */
public class SDefenceFeatureEntity {

    /**
     * eid
     */
    private int eid = 0;

    /**
     * 造成的伤害
     */
    private int lossNum = 0;

    /**
     * 英雄当前血量
     */
    private int currentBloodNum = 0;

    /**
     * 防御力
     */
    private int defenceNum = 0;

    /**
     * 攻击产生的效果
     */
    private List<SAttackEffectTypeEnum> effectList;

    /**
     * 防御效果 - 躲避
     */
    public static final int EFFECT_ID_DEFENCE_DODGE = 1;

    public int getLossNum() {
        return lossNum;
    }

    public void setLossNum(int lossNum) {
        this.lossNum = lossNum;
    }

    public int getDefenceNum() {
        return defenceNum;
    }

    public void setDefenceNum(int defenceNum) {
        this.defenceNum = defenceNum;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getCurrentBloodNum() {
        return currentBloodNum;
    }

    public void setCurrentBloodNum(int currentBloodNum) {
        this.currentBloodNum = currentBloodNum;
    }

    /**
     * 添加一个效果
     * @param effect
     */
    public void addEffect(SAttackEffectTypeEnum effect) {
        if (effectList == null) {
            effectList = new ArrayList<SAttackEffectTypeEnum>();
        }

        effectList.add(effect);
    }

    public List<SAttackEffectTypeEnum> getEffectList() {
        return effectList;
    }
}
