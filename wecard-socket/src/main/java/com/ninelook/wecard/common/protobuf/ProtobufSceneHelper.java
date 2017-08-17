package com.ninelook.wecard.common.protobuf;

import com.ninelook.wecard.common.timer.TimerUtil;
import com.ninelook.wecard.protocol.beans.BeanSceneMessage;
import com.ninelook.wecard.server.scene.player.Player;

/**
 * protobuf场景助手类
 * User: Simon
 * Date: 13-12-29 PM4:28
 */
public class ProtobufSceneHelper {

    /**
     * 返回用户系统信息的Builder
     * @param player
     * @return
     */
    public static BeanSceneMessage.PlayerInfo.Builder getPlayerInfo(Player player) {
        BeanSceneMessage.PlayerInfo.Builder playerInfoBuilder = BeanSceneMessage.PlayerInfo.newBuilder();
        playerInfoBuilder.setUid(player.getUid());
        playerInfoBuilder.setHomeId(player.getHomeId());
        playerInfoBuilder.setStatus(player.getStatus().getIndex());

        return playerInfoBuilder;
    }

    /**
     * 返回用户基本信息的Builder
     * @param joinPlayerUid
     * @return
     */
    public static BeanSceneMessage.UserInfo.Builder getUserInfo(long joinPlayerUid) {
        BeanSceneMessage.UserInfo.Builder userInfoBuilder = BeanSceneMessage.UserInfo.newBuilder();
        userInfoBuilder.setExp(10);
        userInfoBuilder.setLevel(1);
        userInfoBuilder.setMasterHeroId(1);
        userInfoBuilder.setMasterHeroLevel(1);
        userInfoBuilder.setName("simon");
        userInfoBuilder.setUid(9001);

        return userInfoBuilder;
    }

    /**
     * 返回服务器信息的Builder
     * @return
     */
    public static BeanSceneMessage.ServerInfo.Builder getServerInfo() {
        BeanSceneMessage.ServerInfo.Builder serverInfoBuilder = BeanSceneMessage.ServerInfo.newBuilder();

        serverInfoBuilder.setCurrentTimeMillis(TimerUtil.getMSTime());

        return serverInfoBuilder;
    }
}
