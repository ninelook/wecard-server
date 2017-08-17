package com.ninelook.wecard.server.scene.handler.impl.home.async.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufSceneHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanSceneMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.impl.home.async.message.AsyncHomePlayerInnerMessage;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 异步内部接口 - 用户基本信息获取
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class AsyncHomePlayerInfoInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(AsyncHomePlayerInfoInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        AsyncHomePlayerInnerMessage innerMessage = (AsyncHomePlayerInnerMessage) nMessage;
        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();
        List<Long> playerList = innerMessage.getPlayerList();

        logger.info("HomePlayerInfoInnerHandler ... uid:" + uid + ", playerList:" + playerList);

        //没有加入任何房间则报错
        if (homeId <= 0) {
            throw new NException(NException.SCENE_HOME_PLAYER_JOIN_NOTHING);
        }

        //房间不存在则报错
        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);
        if (homeWorker == null) {
            throw new NException(NException.SCENE_HOME_NOT_EXISTS);
        }

        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage();

        for (Long joinPlayerUid : playerList) {
            BeanSceneMessage.UserInfo.Builder userInfoBuilder = ProtobufSceneHelper.getUserInfo(joinPlayerUid);
            messageResBuilder.addLUserInfo(userInfoBuilder);
        }

        ClientManager.getInstance().facadeSend(uid, messageResBuilder.build());
    }
}
