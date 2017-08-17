package com.ninelook.wecard.server.scene.handler.impl.scene;

import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.impl.home.inner.message.HomeExitInnerMessage;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 处理器 - 用户下线
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class SceneOfflineHandler implements Handler {
    static Logger logger = LogManager.getLogger(SceneOfflineHandler.class.getName());

    public void handle(NMessage message) {
        long uid = message.getUid();

        logger.info("SceneOfflineHandler ... uid:" + uid);

        int homeId = PlayerManager.getInstance().getPlayer(uid).getHomeId();

        //用户房间处理
        if (homeId > 0) {
            HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);
            if (homeWorker != null) {
                //用户退出房间
                HomeExitInnerMessage homeExitInnerMessage = new HomeExitInnerMessage(uid, homeId, true);
                homeExitInnerMessage.exeHandler();
            }
        }

        //ClientManager
        ClientManager.getInstance().facadeUnregisterClient(uid);

        //用户管理器
        PlayerManager.getInstance().removePlayer(uid);


    }
}
