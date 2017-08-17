package com.ninelook.wecard.server.service.handler.home.inner.handler;

import com.ninelook.wecard.server.scene.async.AsyncProcessor;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.handler.home.async.message.AsyncHomeInitInnerMessage;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomeInitInnerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 房间初始化
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SHomeInitInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SHomeInitInnerHandler.class.getName());

    public void handle(NMessage nMessage) {

        SHomeInitInnerMessage innerMessage = (SHomeInitInnerMessage) nMessage;

        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();
        int mapId = innerMessage.getMapId();

        /**
         * 系统级 - BEGIN
         */

        //清除container容器
        SContainerManager.clear();

        /**
         * 系统级 - END
         */

        logger.info("SHomeInitHandler ... uid:" + uid);

        //房间初始化
        List<Long> joinPlayerList = innerMessage.getJoinPlayerList();

        AsyncHomeInitInnerMessage mes = new AsyncHomeInitInnerMessage(uid, homeId, mapId);
        mes.setPlayerList(joinPlayerList);

        AsyncProcessor.getInstance().sendMessage(mes);
    }

}
