package com.ninelook.wecard.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.player.Player;

import java.util.List;

/**
 * protobuf房间助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class ProtobufHomeHelper {

    /**
     * 返回一个房间的home信息
     * @param homeWorker
     * @return
     */
    public static BeanHomeMessage.HomeInfo.Builder getHomeInfo(HomeWorker homeWorker) {
        BeanHomeMessage.HomeInfo.Builder homeInfoBuilder = BeanHomeMessage.HomeInfo.newBuilder();
        homeInfoBuilder.setHomeId(homeWorker.getHomeId());
        homeInfoBuilder.setOwnerUid(homeWorker.getOwnerUid());
        homeInfoBuilder.setStatus(homeWorker.getStatus().getIndex());

        for (Player p : homeWorker.getJoinPlayersList()) {
            homeInfoBuilder.addJoinPlayerList(ProtobufSceneHelper.getPlayerInfo(p));
        }

        return homeInfoBuilder;
    }

    /**
     * 返回指定地图下的房间列表信息
     * @return
     */
    public static BeanHomeMessage.MapHomeListInfo.Builder getHomeList(List<HomeWorker> homeWorkerList) {
        BeanHomeMessage.MapHomeListInfo.Builder mapHomeListInfo = BeanHomeMessage.MapHomeListInfo.newBuilder();

        for (HomeWorker homeWorker : homeWorkerList) {
            BeanHomeMessage.MapHomeInfo.Builder mapHomeInfo = BeanHomeMessage.MapHomeInfo.newBuilder();
            mapHomeInfo.setHomeId(homeWorker.getHomeId());
            mapHomeInfo.setOwnerId(homeWorker.getOwnerUid());
            mapHomeInfo.setMapId(homeWorker.getMapId());
            mapHomeInfo.setStatus(homeWorker.getStatus().getIndex());
            mapHomeInfo.setPlayerCount(homeWorker.getJoinPlayersNum());

            mapHomeListInfo.addLMapHomeInfo(mapHomeInfo);
        }

        return mapHomeListInfo;
    }
}
