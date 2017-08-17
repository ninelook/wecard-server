package com.ninelook.wecard.server.service.handler.fight.inner.handler;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanEntityMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufEntityHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.handler.fight.inner.message.SDeadInnerMessage;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import com.ninelook.wecard.server.service.module.postwar.WarProcess;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 实体死亡
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SDeadInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SDeadInnerHandler.class.getName());


    public void handle(NMessage nMessage) {

        SDeadInnerMessage innerMessage = (SDeadInnerMessage) nMessage;

        int eid = innerMessage.getEid();

        int homeId = SHomeModel.getInstance().getHomeId();

        logger.info("SDeadInnerHandler ... homeId:" + homeId + "eid:" + eid);

        SUnit sCharacter = (SUnit)SEntityManager.getInstance().getEntity(eid);

        //验证战斗实体是否死亡
        if (sCharacter.isAlive()) {
            throw new NException(NException.SERVICE_ENTITY_NOT_DEAD);
        }

        //针对当前主将死亡信息进行广播
        BeanEntityMessage.DeadInfo.Builder deadInfoBuilder = SProtobufEntityHelper.getDeadInfo(eid);

        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setDeadInfo(deadInfoBuilder);

        SGatewayHelper.sendMessageToHome(heshResMessage.build());

        //进行副本是否已经完成的处理
        WarProcess.getInstance().finishProcess(eid);
    }
}
