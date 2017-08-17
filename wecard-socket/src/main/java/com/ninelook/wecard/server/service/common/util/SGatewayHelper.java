package com.ninelook.wecard.server.service.common.util;

import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

import java.util.List;

/**
 * 网管助手类
 * User: Simon
 * Date: 14-1-4 下午10:48
 */
public class SGatewayHelper {
    /**
     * 发送信息给指定用户
     */
    public static void sendMessageToPlayer(long uid, Response.HeshResMessage message) {
        ClientManager.getInstance().facadeSend(uid, message);
    }

    /**
     * 发送信息给指定房间所有人
     */
    public static void sendMessageToHome(Response.HeshResMessage message) {
        List<Long> uids = SHomeModel.getInstance().getJoinPlayerList();
        ClientManager.getInstance().facadeSendSome(uids, message);
    }
}
