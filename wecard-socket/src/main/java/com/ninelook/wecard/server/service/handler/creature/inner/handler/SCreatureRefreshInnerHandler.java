package com.ninelook.wecard.server.service.handler.creature.inner.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.handler.creature.inner.message.SCreatureRefreshInnerMessage;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 野怪刷新推送
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SCreatureRefreshInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SCreatureRefreshInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        int homeId = SHomeModel.getInstance().getHomeId();
        SCreatureRefreshInnerMessage innerMessage = (SCreatureRefreshInnerMessage) nMessage;


        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();

        //标记为推送型消息
        heshResMessage.setPush(true);

        //通知当前房间所有玩家
        SGatewayHelper.sendMessageToHome(heshResMessage.build());

        logger.info("STickerNoticeInnerHandler.handle ... homeId:" + homeId);

    }

}
