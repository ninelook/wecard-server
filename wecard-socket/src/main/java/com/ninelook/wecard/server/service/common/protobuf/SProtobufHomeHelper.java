package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanBuildMessage;
import com.ninelook.wecard.protocol.beans.BeanCreatureMessage;
import com.ninelook.wecard.protocol.beans.BeanHeroMessage;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.home.entity.SMapEntity;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * protobuf房间助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufHomeHelper {

    /**
     * 返回指定房间的战场信息
     * @param homeId
     * @return
     */
    public static BeanHomeMessage.WarInfo.Builder getWarInfo(int homeId) {
        BeanHomeMessage.WarInfo.Builder warInfoBuilder = BeanHomeMessage.WarInfo.newBuilder();

        //设置房间ID
        warInfoBuilder.setHomeId(homeId);

        //设置地图信息
        BeanHomeMessage.MapInfo.Builder mapInfoBuilder = getMapInfo(homeId);
        warInfoBuilder.setMapInfo(mapInfoBuilder);

        //设置战斗实体
        for (Map.Entry<Integer, SUnit> en : SHomeModel.getInstance().getJoinEntityMap().entrySet()) {

            //英雄
            if (en.getValue().getType() == SEntityTypeEnum.TYPE_HERO) {
                BeanHeroMessage.HeroInfo.Builder heroInfoBuilder = SProtobufHeroHelper.getHeroInfo(en.getKey());
                warInfoBuilder.addHeroInfoList(heroInfoBuilder);
            }

            //野怪
            if (en.getValue().getType() == SEntityTypeEnum.TYPE_CREATURE) {
                BeanCreatureMessage.CreatureInfo.Builder creatureInfoBuilder = SProtobufCreatureHelper.getCreatureInfo(en.getKey());
                warInfoBuilder.addCreatureInfoList(creatureInfoBuilder);
            }

            //建筑
            if (en.getValue().getType() == SEntityTypeEnum.TYPE_BUILD) {
                BeanBuildMessage.BuildInfo.Builder buildInfoBuilder = SProtobufBuildHelper.getBuildInfo(en.getKey());
                warInfoBuilder.addBuildInfoList(buildInfoBuilder);
            }
        }

        return warInfoBuilder;
    }

    /**
     * 返回指定房间的地图信息
     * @param homeId
     * @return
     */
    public static BeanHomeMessage.MapInfo.Builder getMapInfo(int homeId) {

        SMapEntity mapEntity = SHomeModel.getInstance().getMapEntity();

        //设置地图信息
        BeanHomeMessage.MapInfo.Builder mapInfoBuilder = BeanHomeMessage.MapInfo.newBuilder();
        mapInfoBuilder.setMapId(mapEntity.getMapId());
        mapInfoBuilder.setWidth(mapEntity.getWidth());

        return mapInfoBuilder;
    }

    /**
     * 返回战斗结束信息
     * @return
     */
    public static BeanHomeMessage.WarFinishInfo.Builder getWarFinishInfo(boolean war, List<BeanHomeMessage.WarAwardInfo> warAwardInfoList) {
        BeanHomeMessage.WarFinishInfo.Builder warFinishBuilder = BeanHomeMessage.WarFinishInfo.newBuilder();
        warFinishBuilder.setWin(war);
        warFinishBuilder.addAllWarAwardInfo(warAwardInfoList);
        warFinishBuilder.addAllLAward(new ArrayList<Integer>());
        return warFinishBuilder;
    }
}
