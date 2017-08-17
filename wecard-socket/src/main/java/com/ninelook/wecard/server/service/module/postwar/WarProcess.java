package com.ninelook.wecard.server.service.module.postwar;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.service.common.model.SAbstractModel;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufHomeHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.module.entity.SEntityConstant;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.hero.SHeroEntity;
import com.ninelook.wecard.server.service.module.hero.SHeroTypeEnum;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 战后处理类
 * User: Simon
 * Date: 3/26/14 18:25
 */
public class WarProcess extends SAbstractModel {
    static Logger logger = LogManager.getLogger(WarProcess.class.getName());

    /**
     * 结算数值类型 -- 伤害
     */
    public static final int SETTLEMENT_TYPE_HARM = 1;

    /**
     * 结算数值类型 -- 受伤
     */
    public static final int SETTLEMENT_TYPE_WOUND = 2;

    /**
     * 结算数值类型 -- 技能使用次数
     */
    public static final int SETTLEMENT_TYPE_SKILL_USE_NUM = 3;

    /**
     * 结算数值类型 -- 治疗
     */
    public static final int SETTLEMENT_TYPE_TREAT_NUM = 4;


    /**
     * 获取SCreatureManager
     * @return
     */
    public static WarProcess getInstance() {
        WarProcess model = SContainerManager.getInstance().get(SAbstractModel.COMMON_UID, WarProcess.class);
        if (model == null) {
            model = new WarProcess();
            SContainerManager.getInstance().set(SAbstractModel.COMMON_UID, model);
        }
        return model;
    }

    public WarProcess() {
        super(SAbstractModel.COMMON_UID);
    }

    /**
     * 战斗结束处理接口
     * @param deadEid
     */
    public void finishProcess(int deadEid) {
        SUnit deadEntity = (SUnit)SEntityManager.getInstance().getEntity(deadEid);

        if(deadEntity == null)
            return;

        Map<Integer, SUnit> JoinEntityMap = SHomeModel.getInstance().getJoinEntityMap();

        if (JoinEntityMap.size() <= 0)
            return;

        //战斗是否胜利
        WarResultEnum warResult = WarResultEnum.RES_UNOVER;

        //玩家英雄死亡
        if (deadEntity.getType() == SEntityTypeEnum.TYPE_HERO) {
            boolean heroAlive = false;
            for (Map.Entry<Integer, SUnit> en : JoinEntityMap.entrySet()) {
                if (en.getValue().getType() != SEntityTypeEnum.TYPE_HERO) {
                    continue;
                }

                SHeroEntity sHeroEntity = (SHeroEntity) en.getValue();
                if (sHeroEntity.getHeroType() != SHeroTypeEnum.TYPE_HERO_USER) {
                    continue;
                }

                if (sHeroEntity.isAlive()) {
                    heroAlive = true;
                }
            }

            if (heroAlive == false) {
                warResult = WarResultEnum.RES_LEFT_HERO;
            }
        }

        //处理战斗结果
        dealWarResult(warResult);
    }

    /**
     * 处理战斗结果
     * @param warResult
     */
    public void dealWarResult(WarResultEnum warResult) {
        //没有达成战斗结束条件则返回
        if (warResult == WarResultEnum.RES_UNOVER)
            return;

        //判断胜利方
        boolean isWar;
        if (warResult == WarResultEnum.RES_LEFT_BUILD || warResult == WarResultEnum.RES_LEFT_HERO || warResult == WarResultEnum.RES_LEFT_LOSE_TRIGGER) {
            isWar = false;
        } else {
            isWar = true;
        }

        SHomeModel sHomeModel = SHomeModel.getInstance();

        int mapId = sHomeModel.getMapEntity().getMapId();
        List<Long> joinPlayerList = sHomeModel.getJoinPlayerList();

        //停止所有实体的战斗, 改变房间状态
        SHomeModel.getInstance().setFinish(true);
    }

    /**
     * 推送战斗结束信息
     * @param win                               是否胜利
     * @param warAwardInfoList                奖励列表
     */
    public void pushWarFinish(boolean win, List<BeanHomeMessage.WarAwardInfo> warAwardInfoList) {
        logger.info("WarProcess.pushWarFinish ... push war Finish data[war:" + win +", awardList:" + warAwardInfoList);

        //针对当前实体信息进行广播
        BeanHomeMessage.WarFinishInfo.Builder warFinishInfo = SProtobufHomeHelper.getWarFinishInfo(win, warAwardInfoList);

        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setWarFinishInfo(warFinishInfo);
        heshResMessage.setPush(true);

        SGatewayHelper.sendMessageToHome(heshResMessage.build());
    }


    /**
     * 增加英雄结算数值
     * @param eid
     * @param settlementList
     */
    public static void addSettlementNum(int eid, Map<Integer, Integer> settlementList) {
        if (SEntityManager.getInstance().getEntity(eid) == null
                || SEntityManager.getInstance().getEntity(eid).getType() != SEntityTypeEnum.TYPE_HERO
                || settlementList == null || settlementList.isEmpty()) {
            return;
        }

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        if (sHeroEntity.getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        for (Iterator iterator = settlementList.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

            if (entry.getValue() <= 0) {
                continue;
            }

            switch (entry.getKey().intValue()) {
                case SETTLEMENT_TYPE_HARM:
                    sHeroEntity.addHarmNum(entry.getValue());
                    break;
                case SETTLEMENT_TYPE_WOUND:
                    sHeroEntity.addWoundNum(entry.getValue());
                    break;
                case SETTLEMENT_TYPE_SKILL_USE_NUM:
                    sHeroEntity.addUseSkillNum(entry.getValue());
                    break;
                case SETTLEMENT_TYPE_TREAT_NUM:
                    sHeroEntity.addTreatNum(entry.getValue());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 增加英雄结算伤害数值
     * @param eid
     * @param harmNum
     */
    public static void addHarmNum(int eid, int harmNum) {
        if (SEntityManager.getInstance().getEntity(eid) == null
                || SEntityManager.getInstance().getEntity(eid).getType() != SEntityTypeEnum.TYPE_HERO
                || harmNum <= 0) {
            return;
        }

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        if (sHeroEntity.getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        sHeroEntity.addHarmNum(harmNum);
    }

    /**
     * 增加英雄结算受伤数值
     * @param eid
     * @param woundNum
     */
    public static void addWoundNum(int eid, int woundNum) {
        if (SEntityManager.getInstance().getEntity(eid) == null
                || SEntityManager.getInstance().getEntity(eid).getType() != SEntityTypeEnum.TYPE_HERO
                || woundNum <= 0) {
            return;
        }

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        if (sHeroEntity.getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        sHeroEntity.addWoundNum(woundNum);
    }

    /**
     * 增加英雄结算技能使用次数
     * @param eid
     * @param skillUseNum
     */
    public static void addSkillUseNum(int eid, int skillUseNum) {
        if (SEntityManager.getInstance().getEntity(eid) == null
                || SEntityManager.getInstance().getEntity(eid).getType() != SEntityTypeEnum.TYPE_HERO
                || skillUseNum <= 0) {
            return;
        }

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        if (sHeroEntity.getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        sHeroEntity.addUseSkillNum(skillUseNum);
    }

    /**
     * 增加英雄结算治疗数值
     * @param eid
     * @param treatNum
     */
    public static void addTreatNum(int eid, int treatNum) {
        if (SEntityManager.getInstance().getEntity(eid) == null
                || SEntityManager.getInstance().getEntity(eid).getType() != SEntityTypeEnum.TYPE_HERO
                || treatNum <= 0) {
            return;
        }

        SHeroEntity sHeroEntity = (SHeroEntity)SEntityManager.getInstance().getEntity(eid);

        if (sHeroEntity.getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        sHeroEntity.addTreatNum(treatNum);
    }
}
