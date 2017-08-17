package com.ninelook.wecard.server.scene.dispatcher.impl;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.dispatcher.Dispatcher;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 房间内部消息派发器
 * User: Simon
 * Date: 13-12-25 PM4:17
 */
public class ServiceDispatcherImpl implements Dispatcher {

    static Logger logger = LogManager.getLogger(ServiceDispatcherImpl.class.getName());

    /**
     * 派发消息
     */
    public void process(NMessage message) {

        try {
            logger.info("ServiceDispatcherImpl.process ... uid:" + message.getUid()
                             + ", mid:" + message.getMid()
                             + ", homeId:" + message.getHeshReqMessage().getServiceReqMessage().getHomeId());

            int homeId = message.getHeshReqMessage().getServiceReqMessage().getHomeId();
            long uid = message.getUid();

            //验证房间ID
            if (homeId <= 0) {
                throw new NException(NException.SCENE_HOME_ID_ERROR);
            }

            //验证房间是否存在
            HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);
            if (homeWorker == null) {
                throw new NException(NException.SCENE_HOME_NOT_EXISTS);
            }

            //验证房间内是否有此人
            if (!homeWorker.hasPlayerInJoinPlayers(uid)) {
                throw new NException(NException.SCENE_HOME_PLAYER_NOT_EXISTS);
            }

            //将当前消息发给此房间
            homeWorker.sendMessage(message);

        } catch (NException e) {
            NException.catchErr(message.getUid(), "ServiceDispatcherImpl.process",
                                        Integer.valueOf(e.getMessage()), e, message.getChannel());

        } catch (Exception e) {
            NException.catchErr(message.getUid(), "ServiceDispatcherImpl.process",
                                        NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
        }
    }

}
