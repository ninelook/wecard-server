package com.ninelook.wecard.server.scene.handler.impl.home.inner.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.impl.home.inner.message.HomeExitInnerMessage;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomeExitInnerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 内部接口 - 用户退出房间
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class HomeExitInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeExitInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        HomeExitInnerMessage innerMessage = (HomeExitInnerMessage) nMessage;
        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();

        logger.info("HomeExitInnerHandler ... uid:" + uid + ", homeId:" + homeId);

        //没有加入任何房间则报错
        if (homeId <= 0) {
            throw new NException(NException.SCENE_HOME_PLAYER_JOIN_NOTHING);
        }

        //房间不存在则报错
        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);
        if (homeWorker == null) {
            throw new NException(NException.SCENE_HOME_NOT_EXISTS);
        }

        List<Long> uidList = homeWorker.getJoinUidList();

        //通知服务层
        if(HomeManager.getInstance().getHomeWorker(homeId).getStatus() == HomeStatusEnum.FIGHT_DOING) {
            SHomeExitInnerMessage serviceInnerMessage = new SHomeExitInnerMessage(uid, homeId, innerMessage.isOffline());
            homeWorker.sendMessage(serviceInnerMessage);
        }


        //用户退出房间
        int playerNum = HomeManager.getInstance().exitHome(homeId, uid);

        //房间人数为0则关闭房间
        if (playerNum <= 0) {
            HomeManager.getInstance().closeHome(homeId);
        }

        //转交房间所有权
        long newHomeOnwerUid = 0;
        if (playerNum > 0 && homeWorker.getOwnerUid() == uid) {
            newHomeOnwerUid = homeWorker.transmitHomeToOtherUid();
        }

        //广播退出房间信息
        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage();
        BeanHomeMessage.HomeExitInfo.Builder homeExitInfoBuilder = BeanHomeMessage.HomeExitInfo.newBuilder();

        homeExitInfoBuilder.setNewHomeOnwerUid(newHomeOnwerUid);
        homeExitInfoBuilder.setOffline(innerMessage.isOffline());
        homeExitInfoBuilder.setExitUid(uid);

        messageResBuilder.setHomeExitInfo(homeExitInfoBuilder);

        ClientManager.getInstance().facadeSendSome(uidList, messageResBuilder.build());

    }
}
