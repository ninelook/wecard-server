package com.ninelook.wecard.server.scene.handler.impl.scene;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.protobuf.ProtobufSceneHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanSceneMessage;
import com.ninelook.wecard.server.NContext;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.Player;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 处理器 - 场景登陆
 * User: Simon
 * Date: 13-12-17 PM10:57
 */
public class SceneLoginHandler implements Handler {
    static Logger logger = LogManager.getLogger(SceneLoginHandler.class.getName());

    public void handle(NMessage message) {
        long uid = message.getUid();

        logger.info("SceneLoginHandler ... uid:" + uid);

        //添加到玩家管理器
        Player player = new Player(uid);
        NContext.getActx().getBean(PlayerManager.class).addPlayer(player);

        /**
         * protobuf
         */

        Response.HeshResMessage.Builder messageResBuilder = ProtobufCoreHelper.getHeshResMessage(message.getMid());

        //设置用户信息
        BeanSceneMessage.PlayerInfo.Builder playerInfoBuilder = ProtobufSceneHelper.getPlayerInfo(player);
        messageResBuilder.setPlayerInfo(playerInfoBuilder);

        //设置服务器信息
        BeanSceneMessage.ServerInfo.Builder serverInfoBuilder = ProtobufSceneHelper.getServerInfo();
        messageResBuilder.setServerInfo(serverInfoBuilder);

        //ClientManager
        ClientManager.getInstance().facadeSend(uid, messageResBuilder.build());
    }
}
