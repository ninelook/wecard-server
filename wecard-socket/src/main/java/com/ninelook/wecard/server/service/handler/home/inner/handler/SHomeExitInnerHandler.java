package com.ninelook.wecard.server.service.handler.home.inner.handler;

import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.handler.home.inner.message.SHomeExitInnerMessage;
import com.ninelook.wecard.server.service.module.entity.SEntityConstant;
import com.ninelook.wecard.server.service.module.hero.SHeroEntity;
import com.ninelook.wecard.server.service.module.hero.SHeroManager;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 房间退出
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SHomeExitInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(SHomeExitInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        SHomeExitInnerMessage innerMessage = (SHomeExitInnerMessage) nMessage;
        long uid = innerMessage.getUid();
        int homeId = innerMessage.getHomeId();

        logger.info("SHomeExitInnerHandler ... uid:" + uid + ", homeId=" + homeId);

        SHomeModel homeModel = SHomeModel.getInstance();

        SHeroEntity sHeroEntity = SHeroManager.getInstance(uid).getMainHeroEntity();
        sHeroEntity.setStatus(SEntityConstant.SENTITY_STATUS_OFFLINE);

        /**
         * 系统级 - BEGIN
         */

        if (homeModel.getJoinEntityMap().size() <= 0) {

            //清除container容器
            SContainerManager.clear();
        }

        /**
         * 系统级 - END
         */




    }
}
