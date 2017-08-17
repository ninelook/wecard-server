package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufHomeHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.Player;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import com.ninelook.wecard.server.scene.player.PlayerStatusEnum;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 建立一个房间
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeCreateHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeCreateHandler.class.getName());

    public void handle(NMessage message){
        long uid = message.getUid();
        int mapId = message.getHeshReqMessage().getSceneReqMessage().getReqHomeCreate().getMapId();

        logger.info("HomeCreateHandler ... uid:" + uid);

        HomeManager homeManager = HomeManager.getInstance();

        //验证当前用户是不是已拥有房间
        Player player = PlayerManager.getInstance().getPlayer(uid);
        if (player.getHomeId() > 0) {
            throw new NException(NException.SCENE_HOME_USER_ALREADY_HAVE_HOME);
        }

        HomeWorker homeWorker = homeManager.createHomeWorker(uid, mapId);

        
        //当前用户加入房间内
        homeWorker.joinHome(uid);

        //设置当前用户所属房间ID
        player.setHomeId(homeWorker.getHomeId());

        //改变玩家状态
        player.setStatus(PlayerStatusEnum.READY);

        /**
         * protobuf
         */
        //返回房间信息
        BeanHomeMessage.HomeInfo.Builder homeInfoBuilder = ProtobufHomeHelper.getHomeInfo(homeWorker);

        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage(message.getMid());
        messageResBuilder.setHomeInfo(homeInfoBuilder);

        ClientManager.getInstance().facadeSend(uid, messageResBuilder.build());
    }
}
