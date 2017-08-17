package com.ninelook.wecard.server.service.module.skill.impl;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import com.ninelook.wecard.server.service.module.skill.SSkillAbstract;
import com.ninelook.wecard.server.service.module.skill.SSkillTypeEnum;

/**
 * 召唤类技能
 * User: FLS
 * Date: 5/7/14 11:30
 */
public class SCallUpSkill extends SSkillAbstract<SCallUpResult>{
    /**
     * 要召唤的实体id
     */
    private int callUpServerId;

    /**
     * 存活时间
     */
    private int aliveTime;

    /**
     * 实体位置
     */
    private float startPos;

    /**
     * 召唤类技能
     * @param skillId
     */
    public SCallUpSkill(int skillId, SUnit entity) {
        super(skillId, SSkillTypeEnum.SKILL_TYPE_CALLUP, entity);

        if (entity.getType() == SEntityTypeEnum.TYPE_HERO) {
            initSkill();
            this.startPos = entity.getX() + this.distance;

            SHomeModel sHomeModel = SHomeModel.getInstance();

            if (sHomeModel.getRightX() < this.startPos) {
                this.startPos = sHomeModel.getRightX();
            } else if (sHomeModel.getLeftX() > this.startPos) {
                this.startPos = sHomeModel.getLeftX();
            }
        } else  {
            throw new NException(NException.SERVICE_SKILL_CAN_NOT_USE_CALL_UP_SKILL);
        }
    }

    /**
     * 执行技能
     */
    @Override
    public SCallUpResult execute() {
        return new SCallUpResult();
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
