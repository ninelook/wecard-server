package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.common.timer.TimerUtil;
import com.ninelook.wecard.protocol.beans.BeanEntityMessage;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;

/**
 * protobuf助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufEntityHelper {

    /**
     * 返回指定战斗实体的位置信息
     * @return
     */
    public static BeanEntityMessage.PosInfo.Builder getPosInfo(int eid) {
        BeanEntityMessage.PosInfo.Builder posInfoBuilder = BeanEntityMessage.PosInfo.newBuilder();

        SUnit entity = (SUnit)SEntityManager.getInstance().getEntity(eid);

        posInfoBuilder.setEid(eid);
        posInfoBuilder.setX((int)entity.getX());
        posInfoBuilder.setCurrentTimeMillis(TimerUtil.getMSTime());
        posInfoBuilder.setDir(entity.getMovement().getDir());

        return posInfoBuilder;
    }

    /**
     * 返回死亡信息
     * @return
     */
    public static BeanEntityMessage.DeadInfo.Builder getDeadInfo(int eid) {
        BeanEntityMessage.DeadInfo.Builder DeadInfoBuilder = BeanEntityMessage.DeadInfo.newBuilder();

        DeadInfoBuilder.setEid(eid);

        return DeadInfoBuilder;
    }

    /**
     * 返回实体信息
     * @return
     */
    public static BeanEntityMessage.EntityInfo.Builder getEntityInfo(int eid) {
        SUnit unit = (SUnit)SEntityManager.getInstance().getEntity(eid);

        BeanEntityMessage.EntityInfo.Builder EntityInfoBuilder = BeanEntityMessage.EntityInfo.newBuilder();
        EntityInfoBuilder.setEid(unit.getEid());
        EntityInfoBuilder.setServerId(unit.getServerId());
        EntityInfoBuilder.setType(unit.getType().getIndex());
        EntityInfoBuilder.setLevel(unit.getLevel());
        EntityInfoBuilder.setCamp(unit.getCamp().getIndex());
        EntityInfoBuilder.setSkillId(unit.getSkillId());
        EntityInfoBuilder.setBlood(unit.getBlood());
//        EntityInfoBuilder.setMaxBlood(unit.getMaxBlood());
        EntityInfoBuilder.setAttackNum(unit.getAttackNum());
        EntityInfoBuilder.setDefenceNum(unit.getDefenceNum());
        EntityInfoBuilder.setMagicAttackNum(unit.getMagicAttackNum());
        EntityInfoBuilder.setMagicDefense(unit.getMagicDefense());
        EntityInfoBuilder.setSpeed(unit.getSpeed());
        EntityInfoBuilder.setX((int)unit.getX());

        return EntityInfoBuilder;
    }

}
