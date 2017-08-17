package com.ninelook.wecard.server.service.module.skill.impl;

import com.ninelook.wecard.server.service.module.entity.fight.SFightSerialEntity;
import com.ninelook.wecard.server.service.module.skill.SSkillResult;

import java.util.List;

/**
 * 攻击技能返回结果
 * User: Simon
 * Date: 3/5/14 15:47
 */
public class SAttackSkillResult implements SSkillResult {
    private List<SFightSerialEntity> serialsFightList;

    public List<SFightSerialEntity> getSerialsFightList() {
        return serialsFightList;
    }

    public void setSerialsFightList(List<SFightSerialEntity> serialsFightList) {
        this.serialsFightList = serialsFightList;
    }
}
