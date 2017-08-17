package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufSceneHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.protocol.beans.BeanSceneMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.Player;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import com.ninelook.wecard.server.scene.player.PlayerStatusEnum;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomePlayInnerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 玩家加载结束通知接口
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeLoadingFinishHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeLoadingFinishHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();
        int homeId = message.getHeshReqMessage().getSceneReqMessage().getReqLoadingFinish().getHomeId();

        logger.info("HomeLoadingFinishHandler ... uid:" + uid + ", homeId:" + homeId);

        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);

        if (homeWorker.getStatus() != HomeStatusEnum.DATA_LOADED) {
            throw new NException(NException.SCENE_HOME_STATUS_ERROR);
        }

        //设置当前用户加载完成
        Player player = PlayerManager.getInstance().getPlayer(uid);
        player.setStatus(PlayerStatusEnum.LOADED);

        //广播当前用户加载完成的信息
        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage();
        BeanHomeMessage.LoadedInfo.Builder loadedInfoBuilder = BeanHomeMessage.LoadedInfo.newBuilder();
        loadedInfoBuilder.setHomeId(homeId);
        loadedInfoBuilder.addLUid(uid);

        messageResBuilder.setLoadedInfo(loadedInfoBuilder);
        ClientManager.getInstance().facadeSendSome(homeWorker.getJoinUidList(), messageResBuilder.build());

        //所有玩家都加载完成则通知房间服务层
        if (homeWorker.isLoadedByAllPlayer() == false)
            return;

        //没有加载完成不允许开始进入游戏
        if (homeWorker.getStatus() != HomeStatusEnum.DATA_LOADED) {
            throw new NException(NException.SCENE_HOME_STATUS_ERROR);
        }

        //设置房间状态
        homeWorker.setStatus(HomeStatusEnum.CLIENT_RESOURCE_LOADED);

        //广播当前房间已经开始并发送用户列表
        messageResBuilder = ProtobufCoreHelper.getHeshResMessage();
        BeanHomeMessage.HomeGoInfo.Builder homeGoInfoBuilder = BeanHomeMessage.HomeGoInfo.newBuilder();

        //组织当前房间存在的用户列表
        for (Player p : homeWorker.getJoinPlayersList()) {
            BeanSceneMessage.PlayerInfo.Builder playerInfoBuilder = ProtobufSceneHelper.getPlayerInfo(p);
            homeGoInfoBuilder.addLPlayerInfo(playerInfoBuilder);
        }
        messageResBuilder.setHomeGoInfo(homeGoInfoBuilder);

        ClientManager.getInstance().facadeSendSome(homeWorker.getJoinUidList(), messageResBuilder.build());

        //通知服务层初始化房间
        SHomePlayInnerMessage sHomePlayInnerMessage = new SHomePlayInnerMessage(uid, homeId, homeWorker.getJoinUidList(), homeWorker.getMapId());
        homeWorker.sendMessage(sHomePlayInnerMessage);
    }
}
