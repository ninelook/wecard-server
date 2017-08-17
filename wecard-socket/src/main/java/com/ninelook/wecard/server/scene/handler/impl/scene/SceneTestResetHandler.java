package com.ninelook.wecard.server.scene.handler.impl.scene;

import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 处理器 - 场景登陆
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class SceneTestResetHandler implements Handler {
    static Logger logger = LogManager.getLogger(SceneTestResetHandler.class.getName());

    public void handle(NMessage message) {
        long uid = message.getUid();

        logger.info("SceneTestResetHandler ... uid:" + uid);

        HomeManager.getInstance().getHomeIdGenerator().set(0);

        PlayerManager.getInstance().init();
    }
}
