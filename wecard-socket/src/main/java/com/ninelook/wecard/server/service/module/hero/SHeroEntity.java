package com.ninelook.wecard.server.service.module.hero;

import com.ninelook.wecard.server.service.module.ai.SHeroAI;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;

/**
 * 英雄entity
 * User: Simon
 * Date: 14-1-4 上午11:19
 */
public class SHeroEntity extends SUnit {
    /**
     * 玩家uid
     */
    private long uid;

    private SHeroTypeEnum heroType;

    /**
     * 受伤数量
     */
    private int woundNum;

    /**
     * 伤害数量
     */
    private int harmNum;

    /**
     * 使用技能数量
     */
    private int useSkillNum;

    /**
     * 治疗数量
     */
    private int treatNum;

    public SHeroEntity(long uid) {
        super(SEntityTypeEnum.TYPE_HERO);
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public boolean initAI() {
        if (heroType == SHeroTypeEnum.TYPE_HERO_AI) {
            this.aiEngine = new SHeroAI(this);
        }

        return true;
    }

    /**
     * 各实体需实现的死亡接口
     */
    public void dead() {
        super.dead();

        //死亡但不删除, 复活使用.(AI英雄除外)
        if (heroType == SHeroTypeEnum.TYPE_HERO_AI) {
            SHeroManager.getInstance(uid).removeHero(eid);
        }
    }

    public SHeroTypeEnum getHeroType() {
        return heroType;
    }

    public void setHeroType(SHeroTypeEnum heroType) {
        this.heroType = heroType;
    }

    public int getWoundNum() {
        return woundNum;
    }

    public void setWoundNum(int woundNum) {
        this.woundNum = woundNum;
    }

    public void addWoundNum(int woundNum) {
        this.woundNum += woundNum;
    }

    public int getHarmNum() {
        return harmNum;
    }

    public void setHarmNum(int harmNum) {
        this.harmNum = harmNum;
    }

    public void addHarmNum(int harmNum) {
        this.harmNum += harmNum;
    }

    public int getUseSkillNum() {
        return useSkillNum;
    }

    public void setUseSkillNum(int useSkillNum) {
        this.useSkillNum = useSkillNum;
    }

    public void addUseSkillNum(int useSkillNum) {
        this.useSkillNum += useSkillNum;
    }

    public int getTreatNum() {
        return treatNum;
    }

    public void setTreatNum(int treatNum) {
        this.treatNum = treatNum;
    }

    public void addTreatNum(int treatNum) {
        this.treatNum += treatNum;
    }
}
