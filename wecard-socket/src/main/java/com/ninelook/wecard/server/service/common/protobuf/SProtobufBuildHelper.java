package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanBuildMessage;
import com.ninelook.wecard.server.service.module.build.SBuildEntity;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;

/**
 * protobuf助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufBuildHelper {

    /**
     * 返回指定建筑信息
     * @return
     */
    public static BeanBuildMessage.BuildInfo.Builder getBuildInfo(int eid) {

        SBuildEntity sBuildEntity = (SBuildEntity)SEntityManager.getInstance().getEntity(eid);

        //设置地图信息
        BeanBuildMessage.BuildInfo.Builder buildInfoBuilder = BeanBuildMessage.BuildInfo.newBuilder();

        buildInfoBuilder.setEid(sBuildEntity.getEid());
        buildInfoBuilder.setServerId(sBuildEntity.getServerId());
        buildInfoBuilder.setType(sBuildEntity.getType().getIndex());
        buildInfoBuilder.setLevel(sBuildEntity.getLevel());
        buildInfoBuilder.setCamp(sBuildEntity.getCamp().getIndex());
        buildInfoBuilder.setSkillId(sBuildEntity.getSkillId());
        buildInfoBuilder.setBlood(sBuildEntity.getBlood());
        buildInfoBuilder.setAttackNum(sBuildEntity.getAttackNum());
        buildInfoBuilder.setDefenceNum(sBuildEntity.getDefenceNum());
        buildInfoBuilder.setMagicAttackNum(sBuildEntity.getMagicAttackNum());
        buildInfoBuilder.setMagicDefense(sBuildEntity.getMagicDefense());
        buildInfoBuilder.setSpeed(sBuildEntity.getSpeed());
        buildInfoBuilder.setX((int)sBuildEntity.getX());

        return buildInfoBuilder;
    }

}
