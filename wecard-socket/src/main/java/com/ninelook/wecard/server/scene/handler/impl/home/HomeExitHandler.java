package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.impl.home.inner.message.HomeExitInnerMessage;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 退出房间
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeExitHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeExitHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();
        int homeId = message.getHeshReqMessage().getSceneReqMessage().getReqHomeExit().getHomeId();

        if(homeId <= 0) {
            throw new NException(NException.ERROR_PARAM_VALID_FAIL);
        }

        logger.info("HomeExitHandler ... uid:" + uid + ", homeId:" + homeId);

        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);
        if (homeWorker != null) {
            //用户退出房间
            HomeExitInnerMessage homeExitInnerMessage = new HomeExitInnerMessage(uid, homeId, false);
            homeExitInnerMessage.exeHandler();
        }

    }
}
