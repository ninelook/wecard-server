package com.ninelook.wecard.server.service.handler.home.async.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufHomeHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.handler.home.async.message.AsyncHomeInitInnerMessage;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 异步内部接口 - 房间初始化
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class AsyncHomeInitInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(AsyncHomeInitInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        logger.info("AsyncHomeInitInnerHandler ... begin");

        AsyncHomeInitInnerMessage innerMessage = (AsyncHomeInitInnerMessage) nMessage;
        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();
        int mapId = innerMessage.getMapId();
        List<Long> playerList = innerMessage.getPlayerList();

        SContainerManager.setCtxHomeId(homeId);


        logger.info("AsyncHomeInitInnerHandler ... uid:" + uid + ", playerList:" + playerList);
        SHomeModel homeModel = SHomeModel.getInstance();
        homeModel.homeInit(homeId, mapId, playerList);
        logger.info("AsyncHomeInitInnerHandler ... homeInit finished");


        logger.info("AsyncHomeInitInnerHandler ... init setWarInfo");
        //发送房间战场数据
        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setWarInfo(SProtobufHomeHelper.getWarInfo(homeId));

        logger.info("AsyncHomeInitInnerHandler ... sendMessageToHome");
        //通知当前房间所有玩家
        SGatewayHelper.sendMessageToHome(heshResMessage.build());

        //改变当前房间状态
        HomeManager.getInstance().getHomeWorker(homeId).setStatus(HomeStatusEnum.DATA_LOADED);
        logger.info("AsyncHomeInitInnerHandler ... end");
    }
}
