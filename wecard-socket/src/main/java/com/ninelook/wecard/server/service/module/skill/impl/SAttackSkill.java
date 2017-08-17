package com.ninelook.wecard.server.service.module.skill.impl;

import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.entity.fight.SAttackFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SDefenceFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SFightSerialEntity;
import com.ninelook.wecard.server.service.module.postwar.WarProcess;
import com.ninelook.wecard.server.service.module.skill.SSkillAbstract;
import com.ninelook.wecard.server.service.module.skill.SSkillTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 攻击类技能
 * User: Simon
 * Date: 3/3/14 17:06
 */
public class SAttackSkill extends SSkillAbstract<SAttackSkillResult> {

    /**
     * 攻击距离
     */
    private int distance;

    /**
     * 攻击范围
     */
    private int range;

    /**
     * 伤害类型(0:数值, 1:百分比)
     */
    private int huntType;

    /**
     * 伤害值
     */
    private int attackNum;

    /**
     * 自身伤害值
     */
    private int selfNum;

    /**
     * 伤害类型 - 数值
     */
    public static final int HUNT_TYPE_NUM = 0;

    /**
     * 伤害类型 - 百分比
     */
    public static final int HUNT_TYPE_PERCENT = 1;

    /**
     * 攻击类技能
     * @param skillId
     */
    public SAttackSkill(int skillId, SUnit entity) {
        super(skillId, SSkillTypeEnum.SKILL_TYPE_ATTACK, entity);

        initSkill();
    }

    /**
     * 执行技能
     */
    @Override
    public SAttackSkillResult execute() {
        //组织一次连招攻击数据
        SFightSerialEntity fightSerialEntity = new SFightSerialEntity();
        fightSerialEntity.setSerial(0);

        List<SFightSerialEntity> serialsFightList = new ArrayList<SFightSerialEntity>();

        //设置攻击方攻击效果
        SAttackFeatureEntity attackFeatureEntity = new SAttackFeatureEntity();
        attackFeatureEntity.setAttackNum(this.attackNum);


        fightSerialEntity.setAttackFeature(attackFeatureEntity);

        //总的伤血数量
        int totalHarmBloodNum = 0;

        //遍历并攻击所有玩家
        for (int eid : this.targetEidList) {
            SDefenceFeatureEntity defenceFeatureEntity = attack(eid);

            if (defenceFeatureEntity == null)
                continue;

            fightSerialEntity.addDefenceFeatureEntity(defenceFeatureEntity);

            totalHarmBloodNum += defenceFeatureEntity.getLossNum();
        }

        serialsFightList.add(fightSerialEntity);

        SAttackSkillResult sAttackSkillResult = new SAttackSkillResult();
        sAttackSkillResult.setSerialsFightList(serialsFightList);

        //对自身照成伤害
        selfAttack();

        //增加结算时伤害值
        WarProcess.addHarmNum(this.entity.getEid(), totalHarmBloodNum);

        return sAttackSkillResult;
    }

    /**
     * 对一个敌人执行一次攻击
     * @param eid
     * @return
     */
    protected SDefenceFeatureEntity attack(int eid) {
        SUnit sCharacterEntity = (SUnit)entity;

        int realAttackNum = this.attackNum;

        //泛型攻击的实体
        SUnit caster = (SUnit)SEntityManager.getInstance().getEntity(eid);

        //不对建筑进行攻击
        if (caster.getType() == SEntityTypeEnum.TYPE_BUILD) {
            return null;
        }

        int ack = sCharacterEntity.getAttackNum() > 0 ? sCharacterEntity.getAttackNum() : sCharacterEntity.getMagicAttackNum();

        //百分比伤害类型
        if (huntType == HUNT_TYPE_PERCENT) {
            realAttackNum = (int)((float)ack / 100 * (float)this.attackNum);
        }

        //开始攻击
        return caster.receiveAttack(realAttackNum);
    }

    /**
     * 对自身照成伤害
     */
    protected void selfAttack() {
        if (this.selfNum > 0) {
            SUnit Scharacter = (SUnit)entity;
            Scharacter.receiveAttack(selfNum);
        }
    }

    /**
     * 初始化当前技能
     * @return
     */
    @Override
    protected boolean initSkill() {
        return true;
    }
}
