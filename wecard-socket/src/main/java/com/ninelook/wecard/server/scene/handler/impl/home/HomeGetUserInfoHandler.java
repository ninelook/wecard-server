package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.protocol.apis.ApiHomeMessage;
import com.ninelook.wecard.server.scene.async.AsyncProcessor;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.impl.home.async.message.AsyncHomePlayerInnerMessage;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 获取一个用户的基本信息
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeGetUserInfoHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeGetUserInfoHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();

        ApiHomeMessage.ReqGetUserInfo joinHomeReq = message.getHeshReqMessage().getSceneReqMessage().getReqGetUserInfo();
        int homeId = joinHomeReq.getHomeId();
        List<Long> uidList = joinHomeReq.getLUidList();

        logger.info("HomeGetUserInfoHandler ... uid:" + uid + ", homeId:" + homeId);

        AsyncHomePlayerInnerMessage mes = new AsyncHomePlayerInnerMessage(uid, homeId);
        mes.setPlayerList(uidList);

        AsyncProcessor.getInstance().sendMessage(mes);
    }
}
