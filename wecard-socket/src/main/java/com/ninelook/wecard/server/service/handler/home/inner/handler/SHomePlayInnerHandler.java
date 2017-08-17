package com.ninelook.wecard.server.service.handler.home.inner.handler;

import com.ninelook.wecard.common.timer.TimerUtil;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomePlayInnerMessage;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 进入房间
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SHomePlayInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SHomePlayInnerHandler.class.getName());

    public void handle(NMessage nMessage) {

        SHomePlayInnerMessage innerMessage = (SHomePlayInnerMessage) nMessage;

        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();
        int mapId = innerMessage.getMapId();

        SHomeModel homeModel = SHomeModel.getInstance();

        //设置房间启动时间
        homeModel.setStartTime(TimerUtil.getMSTime());

        //改变当前房间状态
        HomeManager.getInstance().getHomeWorker(homeId).setStatus(HomeStatusEnum.FIGHT_DOING);
    }

}
