package com.ninelook.wecard.server.service.module.entity;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.common.timer.TimerUtil;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanFightMessage;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufFightHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.module.entity.fight.SAttackFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SDefenceFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SFightSerialEntity;
import com.ninelook.wecard.server.service.module.postwar.WarProcess;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 武器攻击
 * User: Simon
 * Date: 3/23/14 17:35
 */
public class SWeaponAttack {

    static Logger logger = LogManager.getLogger(SWeaponAttack.class.getName());


    protected SUnit unit;

    //最后一次攻击序列
    protected int lastAttackSerial = 0;

    //最后一次攻击时间
    protected long lastAttackTime = 0;

    //普通攻击冷却时间
    protected int normalAttackInterval;

    /**
     * 最小攻击间隔时间
     */
    public static final int NORMAL_ATTACK_INTERVAL_MIN = 300;

    /**
     * 返回普通攻击冷却时间
     * @return
     */
    public int getNormalAttackInterval() {
        return normalAttackInterval;
    }

    /**
     * 设置普通攻击冷却时间
     * @param normalAttackInterval
     */
    public void setNormalAttackInterval(float normalAttackInterval) {
        this.normalAttackInterval = (int)(normalAttackInterval * 1000f);
    }

    public SWeaponAttack(SUnit unit) {
        this.unit = unit;
    }

    /**
     * 当前实体承受一次攻击
     * @return
     */
    public boolean attack(int attackSerial) {
        logger.info("SWeaponAttack.attack ... eid:" + unit.getEid() + ", serial:" + attackSerial);

        long curTime = TimerUtil.getMSTime();

        List<Integer> enemyList = new ArrayList<Integer>(Arrays.asList(new Integer[unit.getEnemyList().size()]));
        Collections.copy(enemyList, unit.getEnemyList());

        //先清除敌人列表, 防止异常后无法复位.
        unit.clearEnemy();

        if (unit.getAtkSkillId() != 0) {
            //释放技能
            return true;
        }

        //验证攻击间隔时间(主要针对玩家控制的英雄, 野怪攻击由AI控制) TODO:启用测试跑的太慢.
//        if (curTime - lastAttackTime < NORMAL_ATTACK_INTERVAL_MIN)
//            throw new NException(NException.SERVICE_FIGHT_ATTACK_INTERVAL_TOO_SHORT);

        lastAttackTime = curTime;

        //连击的第一招, 说明被打断, 复位连招
        if (attackSerial == 1) {
            this.lastAttackSerial = 0;

            //连击招式
        } else {

            //非法招式, 最后一招为空 或 当前招式不是第一招并小于等于最后一招.
            if (this.lastAttackSerial == 0 || attackSerial <= this.lastAttackSerial) {
                //复位连招
                this.lastAttackSerial = 0;

                throw new NException(NException.SERVICE_FIGHT_ILLEGALITY_SERIAL);
            }
        }

        List<SFightSerialEntity> serialsFightList = new ArrayList<SFightSerialEntity>();

        //总的伤血数量
        int totalHarmBloodNum = 0;

        //开始使用所有连招进行攻击
        for (int serial = lastAttackSerial + 1; serial <= attackSerial; serial++) {
            //获取当前招式攻击力
            int serialAttackNum = unit.getSerialAttackNum(serial);

            //组织一次连招攻击数据
            SFightSerialEntity fightSerialEntity = new SFightSerialEntity();
            fightSerialEntity.setSerial(serial);

            //设置攻击方攻击效果
            SAttackFeatureEntity attackFeatureEntity = new SAttackFeatureEntity();
            attackFeatureEntity.setAttackNum(serialAttackNum);
            //todo:如果产生暴击则添加暴击效果
            //attackFeatureEntity.addEffect();
            fightSerialEntity.setAttackFeature(attackFeatureEntity);

            //遍历并攻击所有玩家
            for (int eid : enemyList) {
                //泛型攻击的实体
                SUnit caster = (SUnit)SEntityManager.getInstance().getEntity(eid);

                if (caster == null)
                    continue;

                //开始攻击
                SDefenceFeatureEntity defenceFeatureEntity = caster.receiveAttack(serialAttackNum);

                if (defenceFeatureEntity == null) {
                    continue;
                }

                fightSerialEntity.addDefenceFeatureEntity(defenceFeatureEntity);

                totalHarmBloodNum += defenceFeatureEntity.getLossNum();
            }

            serialsFightList.add(fightSerialEntity);
        }

        //设置最后一次连招
        lastAttackSerial = attackSerial;

        syncFightData(serialsFightList);

        //增加结算时伤害值
        WarProcess.addHarmNum(unit.getEid(), totalHarmBloodNum);

        return true;
    }


    /**
     * 同步战斗数据到玩家
     * @param serialsFightList
     */
    public void syncFightData(List<SFightSerialEntity> serialsFightList) {
        logger.info("SWeaponAttack.syncFightData ... eid:" + unit.getEid());
        BeanFightMessage.FightAttackResultInfo.Builder fightAttackResultInfoBuilder = SProtobufFightHelper.getFightAttackResultInfo(unit.getEid(), serialsFightList);

        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setFightAttackResultInfo(fightAttackResultInfoBuilder);


        SGatewayHelper.sendMessageToHome(heshResMessage.build());
    }
}
