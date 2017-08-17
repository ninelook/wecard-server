package com.ninelook.wecard.server.service.handler.ticker.inner.handler;

import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 滴答消息
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class STickerNoticeInnerHandler implements Handler {
    static Logger logger = LogManager.getLogger(STickerNoticeInnerHandler.class.getName());

    public void handle(NMessage nMessage) {
        SHomeModel homeModel = SHomeModel.getInstance();
        logger.debug("STickerNoticeInnerHandler.handle ... homeId:" + homeModel.getHomeId());

        long currTickTime = System.currentTimeMillis();

        //防止触发消息堆积.
        if (currTickTime - homeModel.prevTickTime <= 30) {
            logger.error("STickerNoticeInnerHandler.handle ... tick message too much!!!!!");

            homeModel.prevTickTime = currTickTime;
            return;
        }

        //上一次tick时间初始化
        if (homeModel.prevTickTime <= 0) {
            homeModel.prevTickTime = currTickTime;
        }

        /**
         * 核心更新方法
         */
        SHomeModel.getInstance().update((int)(currTickTime - homeModel.prevTickTime));

        homeModel.prevTickTime = currTickTime;
    }

}
