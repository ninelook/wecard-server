package com.ninelook.wecard.server.service.module.ai;

import com.ninelook.wecard.server.service.module.entity.SUnit;

import java.util.List;

/**
 * 基础建筑AI
 * User: Simon
 * Date: 3/21/14 10:19
 */
public class SBuildAI extends SUnitAI {
    private int TimerAttackCooling = 0;

    public SBuildAI(SUnit unit) {
        super(unit);
    }

    /**
     * AI更新方法
     * @param diff
     */
    @Override
    public void updateAI(int diff) {
        //是否存活
        if (!unit.isAlive())
            return;

        super.updateAI(diff);

        //周围是否有敌人
        List<SUnit> targetList;
        if ((targetList = findTargetByAttackRange()) == null) {
            return;
        }

        //验证攻击冷却时间
        if (TimerAttackCooling <= diff) {

            //发起攻击
            attack(targetList);

            TimerAttackCooling = unit.getWeaponAttack().getNormalAttackInterval();
        } else TimerAttackCooling -= diff;
    }
}
