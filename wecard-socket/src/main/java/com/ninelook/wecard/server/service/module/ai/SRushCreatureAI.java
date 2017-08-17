package com.ninelook.wecard.server.service.module.ai;

import com.ninelook.wecard.server.service.module.build.SBuildEntity;
import com.ninelook.wecard.server.service.module.build.SBuildManager;
import com.ninelook.wecard.server.service.module.entity.SMovement;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

import java.util.List;

/**
 * 冲锋野怪类
 * User: feiying
 * Date: 2014/5/27 16:34
 */
public class SRushCreatureAI extends SCreatureAI {
    private int TimerAttackCooling = 0;

    private boolean attackFlag = false;

    public SRushCreatureAI(SUnit unit) {
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

        if (!this.attackFlag) {
            int mapId = SHomeModel.getInstance().getMapEntity().getMapId();
            int castleId = 0;

            List<SBuildEntity> buildList = SBuildManager.getInstance().getBuildList();

            if (buildList != null && !buildList.isEmpty()) {
                //当前所处位置
                float curX = unit.getX();

                for (SBuildEntity sBuildEntity : buildList) {
                    float tarX = sBuildEntity.getX();
                    if (sBuildEntity.getServerId() == castleId && tarX >= curX && tarX <= curX + unit.getDistance()) {
                        this.attackFlag = true;
                        break;
                    }
                }
            }
        }

        //周围是否有敌人
        List<SUnit> targetList;
        if (this.attackFlag && (targetList = findTargetByAttackRange()) != null) {
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
