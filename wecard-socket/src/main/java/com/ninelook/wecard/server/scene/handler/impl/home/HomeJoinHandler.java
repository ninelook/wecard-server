package com.ninelook.wecard.server.scene.handler.impl.home;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufHomeHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.apis.ApiHomeMessage;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.Player;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import com.ninelook.wecard.server.scene.player.PlayerStatusEnum;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 加入一个房间
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class HomeJoinHandler implements Handler {
    static Logger logger = LogManager.getLogger(HomeJoinHandler.class.getName());


    public void handle(NMessage message) {
        long uid = message.getUid();

        ApiHomeMessage.ReqHomeJoin joinHomeReq = message.getHeshReqMessage().getSceneReqMessage().getReqHomeJoin();
        int homeId = joinHomeReq.getHomeId();

        logger.info("HomeJoinHandler ... uid:" + uid + ", homeId:" + homeId);

        //加入房间
        HomeWorker homeWorker = HomeManager.getInstance().getHomeWorker(homeId);

        //房间状态不允许玩家加入
        if (homeWorker.getStatus() != HomeStatusEnum.PLAYER_READY_DOING) {
            throw new NException(NException.SCENE_HOME_JOIN_STATUS_ERROR);
        }

        //当前用户加入房间
        homeWorker.joinHome(uid);

        //改变当前玩家状态
        Player player = PlayerManager.getInstance().getPlayer(uid);
        player.setStatus(PlayerStatusEnum.READY);

        //设置当前用户所属房间ID
        player.setHomeId(homeWorker.getHomeId());

        /**
         * protobuf
         */
        //返回房间信息
        BeanHomeMessage.HomeInfo.Builder homeInfoBuilder = ProtobufHomeHelper.getHomeInfo(homeWorker);

        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage(message.getMid());
        messageResBuilder.setHomeInfo(homeInfoBuilder);

        List<Long> uids = new ArrayList();
        for (Player p: homeWorker.getJoinPlayersList()) {
            uids.add(p.getUid());
        }

        //发送给当前房间的玩家
        ClientManager.getInstance().facadeSendSome(uids, messageResBuilder.build());
    }
}
