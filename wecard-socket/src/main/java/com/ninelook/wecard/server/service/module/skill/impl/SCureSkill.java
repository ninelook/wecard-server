package com.ninelook.wecard.server.service.module.skill.impl;

import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.postwar.WarProcess;
import com.ninelook.wecard.server.service.module.skill.SSkillAbstract;
import com.ninelook.wecard.server.service.module.skill.SSkillTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 加血类技能
 * User: Simon
 * Date: 3/3/14 17:06
 */
public class SCureSkill extends SSkillAbstract<SCureSkillResult> {

    /**
     * 加血类型
     */
    private int huntType;

    /**
     * 数量
     */
    private int num;

    /**
     * 加血类技能
     * @param skillId
     */
    public SCureSkill(int skillId, SUnit entity) {
        super(skillId, SSkillTypeEnum.SKILL_TYPE_ADD_BLOOD, entity);

        initSkill();
    }

    /**
     * 执行技能
     */
    @Override
    public SCureSkillResult execute() {
        Map<Integer, Integer> addBloodMap = new HashMap<Integer, Integer>();

        int totalAddNum = 0;

        //遍历并为所有实体加血
        for (int eid : this.targetEidList) {
            int addNum = addBlood(eid);

            if (addNum == 0)
                continue;

            addBloodMap.put(eid, addNum);

            totalAddNum += addNum;
        }

        SCureSkillResult sCureSkillResult = new SCureSkillResult();
        sCureSkillResult.setAddBloodMap(addBloodMap);

        //增加结算时英雄加血数量
        WarProcess.addTreatNum(this.entity.getEid(), totalAddNum);

        return sCureSkillResult;
    }

    /**
     * 对一个实体加一次血
     * @param eid
     * @return
     */
    protected int addBlood(int eid) {
        SUnit sCharacterEntity = (SUnit)entity;

        int tNum = this.num;

        //泛型攻击的实体
        SUnit caster = (SUnit)SEntityManager.getInstance().getEntity(eid);

        //不对建筑进行攻击
        if (caster.getType() == SEntityTypeEnum.TYPE_BUILD) {
            return 0;
        }

        //百分比类型
        if (huntType == HUNT_TYPE_PERCENT) {
            tNum = sCharacterEntity.getBlood() / 100 * this.num;
        }

        //开始加血
        int lastBloodNum = caster.getBlood() + tNum;

        //超过最大血量
        if (lastBloodNum > sCharacterEntity.getMaxBlood()) {
            lastBloodNum = sCharacterEntity.getMaxBlood();
        }

        caster.setBlood(lastBloodNum);

        return tNum;
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
