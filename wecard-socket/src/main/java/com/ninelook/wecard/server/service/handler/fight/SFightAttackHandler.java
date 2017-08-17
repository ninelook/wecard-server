package com.ninelook.wecard.server.service.handler.fight;

import com.ninelook.wecard.protocol.Request;
import com.ninelook.wecard.protocol.beans.BeanFightMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 英雄攻击
 * User: Simon
 * Date: 13-12-27 AM11:37
 */
public class SFightAttackHandler implements Handler {
    static Logger logger = LogManager.getLogger(SFightAttackHandler.class.getName());


    public void handle(NMessage message) {
        //参数处理
        Request.ServiceReqMessage serviceReq = message.getHeshReqMessage().getServiceReqMessage();
        int homeId = serviceReq.getHomeId();
        long uid = message.getUid();

        int attackerEid = serviceReq.getSreqAttack().getAttacker().getEid();

        //攻击承受者列表
        List<BeanFightMessage.FighterInfo> casterList = serviceReq.getSreqAttack().getCasterList();

        //连续攻击序列号
        int serial = serviceReq.getSreqAttack().getSerial();

        logger.info("SFightAttackHandler ... uid:" + uid + ", attackerEid:"
                        + attackerEid +", homeId:" + homeId + ", serial:" + serial + ",casterList:" + casterList.toString());

        //攻击者
        SUnit attackerEntity = (SUnit)SEntityManager.getInstance().getEntity(attackerEid);

        //验证战斗实体是否存活
        if (!attackerEntity.isAlive()) {
            throw new NException(NException.SERVICE_FIGHT_ALREADY_DIED);
        }

        //去除重复防御者eid
        List<Integer> eidList = new ArrayList<Integer>();
        for (BeanFightMessage.FighterInfo caster : casterList ) {
            if (eidList.contains(caster.getEid())) {
                throw new NException(NException.SERVICE_FIGHT_DUPLICATE_CASTER);
            }

            eidList.add(caster.getEid());
        }

        //批量设置攻击者攻击的EID列表
        for (int eid : eidList) {
            attackerEntity.addEnemy(eid);
        }

        attackerEntity.attack(serial);

        //清空敌人列表
        attackerEntity.clearEnemy();
    }
}
