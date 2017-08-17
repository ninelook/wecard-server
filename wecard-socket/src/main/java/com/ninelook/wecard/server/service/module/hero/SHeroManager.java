package com.ninelook.wecard.server.service.module.hero;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.player.EntityCampEnum;
import com.ninelook.wecard.server.service.common.model.SAbstractModel;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 当前用户拥有的英雄
 * User: Simon
 * Date: 13-12-18 AM9:30
 */
public class SHeroManager extends SAbstractModel {

    static Logger logger = LogManager.getLogger(SHeroManager.class.getName());

    /**
     * 英雄列表
     */
    private Map<Integer, SHeroEntity> stHeroEntityMap = new HashMap<Integer, SHeroEntity>();

    /**
     * 主英雄
     */
    private SHeroEntity mainHeroEntity;

    /**
     * 英雄类型 -- 普通
     */
    public static final int HERO_TYPE_NORMAL = 0;

    /**
     * 英雄类型 -- ai
     */
    public static final int HERO_TYPE_AI = 1;

    /**
     * 英雄属性类型列表
     */
    private static final List<Integer> HERO_ATTR_TYPE_LIST = Arrays.asList(

    );

    /**
     * 构造函数 - 加载主将
     * @param uid
     */
    private SHeroManager(long uid) {
        super(uid);
    }

    /**
     * 获取SHeroModel - 主将
     * @param uid
     * @return
     */
    public static SHeroManager getInstance(long uid) {
        SHeroManager model = SContainerManager.getInstance().get(uid, SHeroManager.class);
        if (model == null) {
            model = new SHeroManager(uid);
            SContainerManager.getInstance().set(uid, model);
        }
        return model;
    }

    /**
     * 添加一个英雄
     * @param heroEntity
     */
    public void addHero(SHeroEntity heroEntity){
        //添加到战斗实体管理器
        SEntityManager.getInstance().setEntity(heroEntity.getEid(), heroEntity);

        //添加到当前英雄的英雄列表
        this.stHeroEntityMap.put(heroEntity.getEid(), heroEntity);
    }

    /**
     * 删除一个英雄
     */
    public void removeHero(int eid) {
        this.stHeroEntityMap.remove(eid);

        //场景管理器移除此实体
        SHomeModel.getInstance().removeJoinEntity(eid);

        SEntityManager.getInstance().removeEntity(eid);
    }

    /**
     * 返回一个英雄
     * @param eid
     * @return
     */
    public SHeroEntity getHero(int eid) {
        return this.stHeroEntityMap.get(eid);
    }

    public SHeroEntity getMainHeroEntity() {
        return mainHeroEntity;
    }

    /**
     * 数据读取
     */
    public void loadDataFromMainService() {
        SHeroEntity sHeroEntity = createSHeroEntity(1001, 1, this.uid, SHeroTypeEnum.TYPE_HERO_USER, 10, 10, 10, 10,
                10, 10, 2001, 0);

        //添加到英雄列表
        this.addHero(sHeroEntity);

        //设置主英雄
        this.mainHeroEntity = sHeroEntity;
    }

    /**
     * 创建英雄实体
     * @param serverId
     * @param level
     * @param uid
     * @param heroType
     * @param blood
     * @param attackNum
     * @param defenceNum
     * @param speed
     * @param magicAttackNum
     * @param magicDefence
     * @param skillId
     * @param x
     * @return
     */
    private SHeroEntity createSHeroEntity(int serverId, int level, long uid, SHeroTypeEnum heroType, int blood, int attackNum, int defenceNum,
                                          float speed, int magicAttackNum, int magicDefence, int skillId, int x) {
        if (blood <= 0) {
            throw new NException(NException.SERVICE_LOADDATA_BLOOD_ERROR);
        }

        if (attackNum < 0) {
            throw new NException(NException.SERVICE_LOADDATA_ATTACKNUM_ERROR);
        }

        if (defenceNum < 0) {
            throw new NException(NException.SERVICE_LOADDATA_ATTACKNUM_ERROR);
        }

        if (speed < 0) {
            throw new NException(NException.SERVICE_LOADDATA_SPEED_ERROR);
        }

        SHeroEntity sHeroEntity = new SHeroEntity(uid);

        //设置英雄指定属性
        sHeroEntity.setServerId(serverId);
        sHeroEntity.setLevel(level);
        sHeroEntity.setSkillId(skillId);
        sHeroEntity.setX(x);
        sHeroEntity.setCamp(EntityCampEnum.CAMP_LEFT);

        sHeroEntity.setBlood(blood);
        sHeroEntity.setSrcBlood(blood);
        sHeroEntity.setMaxBlood(blood);

        sHeroEntity.setSpeed(speed);
        sHeroEntity.setSrcSpeed(speed);

        sHeroEntity.setAttackNum(attackNum);
        sHeroEntity.setSrcAttackNum(attackNum);

        sHeroEntity.setDefense(defenceNum);
        sHeroEntity.setSrcDefense(defenceNum);

        sHeroEntity.setMagicAttackNum(magicAttackNum);
        sHeroEntity.setSrcMagicAttackNum(magicAttackNum);

        sHeroEntity.setMagicDefense(magicDefence);
        sHeroEntity.setSrcMagicDefense(magicDefence);
        sHeroEntity.setHeroType(heroType);

        sHeroEntity.init();

        return sHeroEntity;
    }


    /**
     * 房间初始化调用的接口
     * @param homeId
     * @param joinPlayerList
     */
    public static void homeInit(int homeId, List<Long> joinPlayerList) {
        SHomeModel sHomeModel = SHomeModel.getInstance();

        for (Long joinPlayerUid : joinPlayerList) {

            //////////加载数据 - 英雄
            SHeroManager.getInstance(joinPlayerUid).loadDataFromMainService();
            SHeroEntity mainHeroEntity = SHeroManager.getInstance(joinPlayerUid).getMainHeroEntity();

            //所有玩家默认为攻击方
            mainHeroEntity.setCamp(EntityCampEnum.CAMP_LEFT);

            sHomeModel.addJoinEntity(mainHeroEntity);

            //添加玩家到房间
            sHomeModel.addPlayer(joinPlayerUid);

        }
    }

}