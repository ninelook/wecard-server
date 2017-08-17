package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanCreatureMessage;

/**
 * protobuf房间助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufCreatureHelper {

    /**
     * 返回指定用户的主英雄信息
     * @return
     */
    public static BeanCreatureMessage.CreatureInfo.Builder getCreatureInfo(int eid) {

        //设置地图信息
        BeanCreatureMessage.CreatureInfo.Builder creatureInfoBuilder = BeanCreatureMessage.CreatureInfo.newBuilder();

        return creatureInfoBuilder;
    }

}
