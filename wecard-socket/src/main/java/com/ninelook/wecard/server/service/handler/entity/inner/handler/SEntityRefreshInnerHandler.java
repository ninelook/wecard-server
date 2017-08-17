package com.ninelook.wecard.server.service.handler.entity.inner.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanEntityMessage;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufEntityHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.handler.entity.inner.message.SEntityRefreshInnerMessage;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 *  实体刷新推送
 */
public class SEntityRefreshInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SEntityRefreshInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        int homeId = SHomeModel.getInstance().getHomeId();
        SEntityRefreshInnerMessage innerMessage = (SEntityRefreshInnerMessage) nMessage;

        List<SUnit> entityList = innerMessage.getsEntityList();

        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();

        //标记为推送型消息
        heshResMessage.setPush(true);

        for (SUnit sUnit : entityList) {
            BeanEntityMessage.EntityInfo.Builder entityInfoBuilder = SProtobufEntityHelper.getEntityInfo(sUnit.getEid());
            heshResMessage.addEntityRefreshInfo(entityInfoBuilder);
        }

        //通知当前房间所有玩家
        SGatewayHelper.sendMessageToHome(heshResMessage.build());

        logger.info("STickerNoticeInnerHandler.handle ... homeId:" + homeId);
    }
}
