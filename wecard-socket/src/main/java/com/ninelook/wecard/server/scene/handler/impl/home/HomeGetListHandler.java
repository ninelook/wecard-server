package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufHomeHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 获取指定地图下的副本列表信息
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeGetListHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeGetListHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();
        int mapId = message.getHeshReqMessage().getSceneReqMessage().getReqGetList().getMapId();

        logger.info("HomeGetListHandler ... uid:" + uid + ", mapId:" + mapId);

        List<HomeWorker> homeWorkerList = HomeManager.getInstance().getHomeWorkerListByMapId(mapId);

        BeanHomeMessage.MapHomeListInfo.Builder homeListBuilder = ProtobufHomeHelper.getHomeList(homeWorkerList);

        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage();
        messageResBuilder.setMapHomeListInfo(homeListBuilder);

        ClientManager.getInstance().facadeSend(uid, messageResBuilder.build());
    }
}
