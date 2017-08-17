package com.ninelook.wecard.server.service.module.ai;

import com.ninelook.wecard.server.service.module.entity.SMovement;
import com.ninelook.wecard.server.service.module.entity.SUnit;

import java.util.List;

/**
 * 基础野怪AI
 * User: Simon
 * Date: 3/21/14 10:19
 */
public class SCreatureAI extends SUnitAI {
    private int TimerAttackCooling = 0;

    public SCreatureAI(SUnit unit) {
        super(unit);
    }

    /**
     * AI更新方法
     * @param diff
     */
    @Override
    public void updateAI(int diff) {

        //野怪是否存活
        if (!unit.isAlive())
            return;

        //周围是否有敌人
        List<SUnit> targetList;
        if ((targetList = findTargetByAttackRange()) != null) {

            //停止移动
            if (unit.getMovement().getDir() != SMovement.MOVE_DIR_STOP)
                moveStop();

            //验证攻击冷却时间
            if (TimerAttackCooling <= diff) {

                //发起攻击
                attack(targetList);

                TimerAttackCooling = unit.getWeaponAttack().getNormalAttackInterval();
            } else TimerAttackCooling -= diff;

            return;
        }

        //走到终点则暂停移动
        if (moveIsEnd()) {
            moveStop();
            return;
        }

        //继续向前移动
        move();
    }
}
