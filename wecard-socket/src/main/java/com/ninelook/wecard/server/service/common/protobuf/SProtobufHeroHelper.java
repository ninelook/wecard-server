package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanHeroMessage;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.hero.SHeroEntity;

/**
 * protobuf房间助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufHeroHelper {

    /**
     * 返回指定用户的主英雄信息
     * @return
     */
    public static BeanHeroMessage.HeroInfo.Builder getHeroInfo(int eid) {

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        //设置地图信息
        BeanHeroMessage.HeroInfo.Builder heroInfoBuilder = BeanHeroMessage.HeroInfo.newBuilder();

        heroInfoBuilder.setEid(sHeroEntity.getEid());
        heroInfoBuilder.setUid(sHeroEntity.getUid());
        heroInfoBuilder.setCamp(sHeroEntity.getCamp().getIndex());
        heroInfoBuilder.setType(sHeroEntity.getType().getIndex());
        heroInfoBuilder.setServerId(sHeroEntity.getServerId());
        heroInfoBuilder.setLevel(sHeroEntity.getLevel());
        heroInfoBuilder.setSkillId(sHeroEntity.getSkillId());
        heroInfoBuilder.setBlood(sHeroEntity.getBlood());
        heroInfoBuilder.setAttackNum(sHeroEntity.getAttackNum());
        heroInfoBuilder.setDefenceNum(sHeroEntity.getDefenceNum());
        heroInfoBuilder.setMagicAttackNum(sHeroEntity.getMagicAttackNum());
        heroInfoBuilder.setMagicDefense(sHeroEntity.getMagicDefense());
        heroInfoBuilder.setSpeed(sHeroEntity.getSpeed());
        heroInfoBuilder.setX((int)sHeroEntity.getX());

        return heroInfoBuilder;
    }

}
