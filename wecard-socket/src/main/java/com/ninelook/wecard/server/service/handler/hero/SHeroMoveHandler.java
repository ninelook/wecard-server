package com.ninelook.wecard.server.service.handler.hero;

import com.ninelook.wecard.protocol.Request;
import com.ninelook.wecard.protocol.beans.BeanEntityMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.hero.SHeroEntity;
import com.ninelook.wecard.server.service.module.home.entity.SMapEntity;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 英雄移动开始
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SHeroMoveHandler implements Handler {
    static Logger logger = LogManager.getLogger(SHeroMoveHandler.class.getName());


    public void handle(NMessage message) {
        //参数处理
        Request.ServiceReqMessage serviceReq = message.getHeshReqMessage().getServiceReqMessage();
        long uid = message.getUid();
        BeanEntityMessage.PosInfo heroPosInfo = serviceReq.getSreqHeroMove().getHeroPosInfo();

        logger.trace("SHeroMoveHandler ... uid:" + uid + "x:" + heroPosInfo.getX() + ", dir:" + heroPosInfo.getDir());

        int eid = heroPosInfo.getEid();

        int x = heroPosInfo.getX();

        //X位置不可以超出边界.
        if (x < SMapEntity.MAP_LEFT_BORDER || x > SHomeModel.getInstance().getMapEntity().getWidth() - SMapEntity.MAP_RIGHT_BORDER) {
            throw new NException(NException.ERROR_PARAM_VALID_FAIL);
        }


        //位置补偿处理

        //位置效验


        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        //主将进行移动
        sHeroEntity.updateMove(heroPosInfo.getDir(), x);
    }
}
