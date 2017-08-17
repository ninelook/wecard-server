package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomeInitInnerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 开始游戏
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeGoHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeGoHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();
        int homeId = message.getHeshReqMessage().getSceneReqMessage().getReqHomeGo().getHomeId();

        logger.info("HomeGoHandler ... uid:" + uid + ", homeId:" + homeId);

        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);

        //验证用户是否为当前房间的房主
        if (homeWorker.getOwnerUid() != uid) {
            throw new NException(NException.SCENE_HOME_OWNER_ACCORD);
        }

        //设置房间状态
        homeWorker.setStatus(HomeStatusEnum.DATA_LOADING);

        //通知服务层初始化房间
        SHomeInitInnerMessage sHomeInitInnerMessage = new SHomeInitInnerMessage(uid, homeId, homeWorker.getJoinUidList(), homeWorker.getMapId());
        homeWorker.sendMessage(sHomeInitInnerMessage);
    }
}
