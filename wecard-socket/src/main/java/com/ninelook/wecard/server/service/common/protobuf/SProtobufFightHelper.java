package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanFightMessage;
import com.ninelook.wecard.server.service.module.entity.fight.SAttackEffectTypeEnum;
import com.ninelook.wecard.server.service.module.entity.fight.SAttackFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SDefenceFeatureEntity;
import com.ninelook.wecard.server.service.module.entity.fight.SFightSerialEntity;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

import java.util.List;

/**
 * protobuf战斗助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufFightHelper {

    /**
     * 返回指定战斗结果信息
     * @return
     */
    public static BeanFightMessage.FightAttackResultInfo.Builder getFightAttackResultInfo(int attackerEid,List<SFightSerialEntity> FightSerialEntityList) {

        //protobuf
        BeanFightMessage.FightAttackResultInfo.Builder fightAttackResultInfoBuilder = BeanFightMessage.FightAttackResultInfo.newBuilder();
        fightAttackResultInfoBuilder.setEid(attackerEid);
        fightAttackResultInfoBuilder.setSid(SHomeModel.getInstance().getLastSid());


        for(SFightSerialEntity fightSerialEntity : FightSerialEntityList) {
            BeanFightMessage.FightSerialInfo.Builder fightSerialInfoBuilder = BeanFightMessage.FightSerialInfo.newBuilder();

            //攻击序列
            int serial = fightSerialEntity.getSerial();

            //protobuf
            fightSerialInfoBuilder.setSerial(serial);

            //protobuf-attackFeature
            SAttackFeatureEntity attackFeatureEntity = fightSerialEntity.getAttackFeature();

            BeanFightMessage.AttackFeatureInfo.Builder attackFeatureInfoBuilder = BeanFightMessage.AttackFeatureInfo.newBuilder();
            attackFeatureInfoBuilder.setAttackNum(attackFeatureEntity.getAttackNum());

            if (attackFeatureEntity.getEffectList() != null && !attackFeatureEntity.getEffectList().isEmpty()) {
                attackFeatureInfoBuilder.addAllEffect(attackFeatureEntity.getEffectList());
            }

            fightSerialInfoBuilder.setAttackFeatureInfo(attackFeatureInfoBuilder);


            //遍历当前被攻击者攻击序列
            for (SDefenceFeatureEntity defenceFeatureEntity : fightSerialEntity.getDefenceFeaturelist()) {

                BeanFightMessage.DefenceFeatureInfo.Builder defenceFeatureInfoBuilder = BeanFightMessage.DefenceFeatureInfo.newBuilder();
                defenceFeatureInfoBuilder.setLossNum(defenceFeatureEntity.getLossNum());
                defenceFeatureInfoBuilder.setCurrentBloodNum(defenceFeatureEntity.getCurrentBloodNum());
                defenceFeatureInfoBuilder.setEid(defenceFeatureEntity.getEid());

                if (defenceFeatureEntity.getEffectList() != null && !defenceFeatureEntity.getEffectList().isEmpty()) {
                    for ( SAttackEffectTypeEnum e : defenceFeatureEntity.getEffectList()) {
                        defenceFeatureInfoBuilder.addEffect(e.getIndex());
                    }
                }

                fightSerialInfoBuilder.addDefenceFeatureInfo(defenceFeatureInfoBuilder);

            }

            fightAttackResultInfoBuilder.addFightSerialInfo(fightSerialInfoBuilder);
        }

        return fightAttackResultInfoBuilder;
    }


}
