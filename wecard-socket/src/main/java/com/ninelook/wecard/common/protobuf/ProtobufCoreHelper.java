package com.ninelook.wecard.common.protobuf;

import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.protocol.Request;
import com.ninelook.wecard.protocol.Response;

/**
 * 核心信息protobuf类
 * User: Simon
 * Date: 13-12-29 PM5:27
 */
public class ProtobufCoreHelper {
    /**
     * 获取Scene请求类
     * @return
     */
    public static Communication.HeshReqMessage.Builder getHeshReqBySceneMessage(int mid, long uid) {
        Request.SceneReqMessage.Builder sceneReqMessageBuilder = Request.SceneReqMessage.newBuilder();

        Communication.HeshReqMessage.Builder messageBuilder = Communication.HeshReqMessage.newBuilder();
        messageBuilder.setSceneReqMessage(sceneReqMessageBuilder);
        messageBuilder.setMid(mid);
        messageBuilder.setUid(uid);

        return messageBuilder;
    }

    /**
     * 获取Service请求类
     * @return
     */
    public static Communication.HeshReqMessage.Builder getHeshReqByServiceMessage(int mid, long uid, int homeId) {
        Request.ServiceReqMessage.Builder serviceReqMessageBuilder = Request.ServiceReqMessage.newBuilder();

        Communication.HeshReqMessage.Builder messageBuilder = Communication.HeshReqMessage.newBuilder();
        serviceReqMessageBuilder.setHomeId(homeId);
        messageBuilder.setServiceReqMessage(serviceReqMessageBuilder);
        messageBuilder.setMid(mid);
        messageBuilder.setUid(uid);


        return messageBuilder;
    }

    /**
     * 返回一个指定用户的HeshResMessage
     * @return
     */
    public static Response.HeshResMessage.Builder getHeshResMessage() {
        Response.HeshResMessage.Builder messageResBuilder = Response.HeshResMessage.newBuilder();
        messageResBuilder.setMid(0);
        messageResBuilder.setCode(0);

        return messageResBuilder;
    }

    /**
     * 返回一个指定用户指定MID的HeshResMessage
     * @param mid
     * @return
     */
    public static Response.HeshResMessage.Builder getHeshResMessage(int mid) {
        Response.HeshResMessage.Builder messageResBuilder = Response.HeshResMessage.newBuilder();
        messageResBuilder.setMid(mid);
        messageResBuilder.setCode(0);

        return messageResBuilder;
    }
}
