package com.ninelook.wecard.server.service.module.entity.fight;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一招技能的攻击特征
 * User: Simon
 * Date: 14-1-6 下午10:07
 */
public class SFightSerialEntity {
    private int serial;

    /**
     * 攻击特性
     */
    private SAttackFeatureEntity attackFeature;

    /**
     * 受攻击的防御者列表
     */
    private List<SDefenceFeatureEntity> defenceFeaturelist = new ArrayList<SDefenceFeatureEntity>();

    /**
     * 防御特性
     */
    private SDefenceFeatureEntity defenceFeature;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public SAttackFeatureEntity getAttackFeature() {
        return attackFeature;
    }

    public void setAttackFeature(SAttackFeatureEntity attackFeature) {
        this.attackFeature = attackFeature;
    }

    /**
     * 添加一个攻击承受者
     * @param defenceFeatureEntity
     * @return
     */
    public boolean addDefenceFeatureEntity(SDefenceFeatureEntity defenceFeatureEntity) {
        defenceFeaturelist.add(defenceFeatureEntity);
        return true;
    }

    public List<SDefenceFeatureEntity> getDefenceFeaturelist() {
        return defenceFeaturelist;
    }
}
