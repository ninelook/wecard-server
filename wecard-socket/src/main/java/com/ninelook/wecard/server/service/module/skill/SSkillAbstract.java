package com.ninelook.wecard.server.service.module.skill;

import com.ninelook.wecard.server.service.module.entity.SEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能抽象类
 *
 * User: Simon
 * Date: 3/3/14 14:28
 */
public abstract class SSkillAbstract<T extends SSkillResult> {
    /**
     * 技能ID
     */
    protected int skillId;

    /**
     * 技能类型
     */
    protected SSkillTypeEnum type;

    /**
     * 攻击距离
     */
    protected int distance;

    /**
     * 攻击范围
     */
    protected int range;

    /**
     * 伤害类型 - 数值
     */
    public static final int HUNT_TYPE_NUM = 0;

    /**
     * 伤害类型 - 百分比
     */
    public static final int HUNT_TYPE_PERCENT = 1;

    /**
     * 当前技能目标列表
     */
    protected List<Integer> targetEidList = new ArrayList<Integer>();

    /**
     * 当前技能关联的entity
     */
    protected SEntity entity;

    public SSkillAbstract(int skillId, SSkillTypeEnum type) {
        this.skillId = skillId;
        this.type = type;
    }

    public SSkillAbstract(int skillId, SSkillTypeEnum type, SEntity entity) {
        this.skillId = skillId;
        this.type = type;
        this.entity = entity;
    }


    /**
     * 返回当前技能skillId
     * @return
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * 返回当前技能类型
     * @return
     */
    public SSkillTypeEnum getType() {
        return type;
    }

    /**
     * 添加一个作用目标
     * @param eid
     */
    public void addTarget(int eid) {
        if (targetEidList.contains(eid)) {
            return;
        }

        targetEidList.add(eid);
    }

    /**
     * 执行技能
     */
    public abstract T execute();

    /**
     * 初始化当前技能
     * @return
     */
    protected abstract boolean initSkill();
}
