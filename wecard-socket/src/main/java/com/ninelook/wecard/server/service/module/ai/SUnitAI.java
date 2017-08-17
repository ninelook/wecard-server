package com.ninelook.wecard.server.service.module.ai;

import com.ninelook.wecard.server.scene.player.EntityCampEnum;
import com.ninelook.wecard.server.service.module.entity.SMovement;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.home.entity.SMapEntity;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实体AI基础类
 * User: Simon
 * Date: 3/21/14 10:18
 */
public class SUnitAI {
    /**
     * 当前AI关联的实体
     */
    protected SUnit unit;

    /**
     * 找到的玩家临时存储器
     */
    protected List<SUnit> tmpFoundTargetList = new ArrayList<SUnit>();

    public SUnitAI(SUnit unit) {
        this.unit = unit;
    }

    /**
     * 返回当前AI关联的实体
     * @return
     */
    public SUnit getUnit() {
        return unit;
    }

    /**
     * 向默认方向移动
     */
    public void move() {
        if (!unit.isAlive())
            return;

        //获取移动方向
        int dir = unit.getCamp() == EntityCampEnum.CAMP_LEFT ? SMovement.MOVE_DIR_RIGHT : SMovement.MOVE_DIR_LEFT;

        unit.updateMove(dir);
    }

    /**
     * 向指定方向移动
     * @param dir
     */
    public void move(int dir) {
        if (!unit.isAlive())
            return;

        unit.updateMove(dir);
    }

    /**
     * 停止移动
     */
    public void moveStop() {
        if (!unit.isAlive())
            return;

        unit.updateMove(SMovement.MOVE_DIR_STOP);
    }

    /**
     * 判断是否移动到了终点
     */
    public boolean moveIsEnd() {
        if (unit.getCamp() == EntityCampEnum.CAMP_LEFT) {
            return unit.getX() >= SHomeModel.getInstance().getMapEntity().getWidth();
        } else {
            return unit.getX() <= SMapEntity.MAP_LEFT_BORDER;
        }
    }

    /**
     * 查找攻击范围内的敌人
     */
    public List<SUnit> findTargetByAttackRange() {
        tmpFoundTargetList.clear();

        //攻击距离
        int distance = unit.getDistance();

        //当前所处位置
        float curX = unit.getX();

        Map<Integer, SUnit> JoinEntityMap = SHomeModel.getInstance().getJoinEntityMap();

        if (JoinEntityMap.size() <= 0)
            return null;

        for (Map.Entry<Integer, SUnit> en : JoinEntityMap.entrySet()) {
            //跳过死亡实体
            if (!en.getValue().isAlive())
                continue;

            //跳过友方
            if (en.getValue().getCamp() == unit.getCamp())
                continue;

            float tarX = en.getValue().getX();
            if (unit.getCamp() == EntityCampEnum.CAMP_LEFT) {
                if (tarX >= curX && tarX <= curX + distance) {
                    tmpFoundTargetList.add(en.getValue());
                }
            } else {
                if (tarX <= curX && tarX >= curX - distance) {
                    tmpFoundTargetList.add(en.getValue());
                }
            }
        }

        if (tmpFoundTargetList.size() == 0)
            return null;

        return tmpFoundTargetList;
    }

    /**
     * 查找攻击范围内的敌人EID
     * @return
     */
    public List<Integer> findTargetEidByAttackRange() {
        List<SUnit> unitList = findTargetByAttackRange();

        if (unitList == null)
            return null;

        List<Integer> eidList = new ArrayList<Integer>();

        for (SUnit unit : unitList) {
            eidList.add(unit.getEid());
        }

        return eidList;
    }

    /**
     * 开始攻击指定范围内的敌人
     */
    public void attack(List<SUnit> targetList) {
        //判断当前实体是否已经死亡
        if (!unit.isAlive()) {
            return;
        }

        //添加目标敌人
        if (unit.getAttackNum() <= 0 && unit.getMagicAttackNum() <= 0 && unit.getAtkSkillId() <= 0) {
            return;
        }

        for (SUnit target : targetList) {
            if (!target.isAlive())
                continue;

            unit.addEnemy(target.getEid());
        }

        //开始攻击
        unit.attack();

        //攻击结束后清除敌人列表
        unit.clearEnemy();
    }

    /**
     * 添加一系列组件AI
     * @param aiId
     */
    public void addAiPlugin(int aiId) {

    }

    /**
     * AI更新方法
     * @param diff
     */
    public void updateAI(int diff) {
        return;
    }

}
