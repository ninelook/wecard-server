package com.ninelook.wecard.server.service.module.build;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.player.EntityCampEnum;
import com.ninelook.wecard.server.service.common.model.SAbstractModel;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.module.entity.SEntityManager;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建筑全局管理器
 * User: Simon
 * Date: 13-12-18 AM9:30
 */
public class SBuildManager extends SAbstractModel {

    static Logger logger = LogManager.getLogger(SBuildManager.class.getName());

    /**
     * 建筑列表
     */
    private Map<Integer, SBuildEntity> stBuildEntityMap = new HashMap<Integer, SBuildEntity>();

    /**
     * 构造函数
     */
    private SBuildManager() {
        super(SAbstractModel.COMMON_UID);
    }

    /**
     * 获取Manager
     * @return
     */
    public static SBuildManager getInstance() {
        SBuildManager model = SContainerManager.getInstance().get(SAbstractModel.COMMON_UID, SBuildManager.class);
        if (model == null) {
            model = new SBuildManager();
            SContainerManager.getInstance().set(SAbstractModel.COMMON_UID, model);
        }
        return model;
    }

    /**
     * 添加一个建筑
     * @param serverId      serverId
     * @param x             位置
     * @param bornOrderFlag    出生顺序标志
     */
    public SBuildEntity add(int serverId, int x, int blood, int defenceNum, EntityCampEnum camp, boolean bornOrderFlag){
        logger.trace("SBuildManager.add ... add build, serverId:"
                         + serverId + ", x:" + x);
        SBuildEntity entity = new SBuildEntity();

        if (blood <= 0) {
            throw new NException(NException.SERVICE_LOADDATA_BLOOD_ERROR);
        }

        if (defenceNum < 0) {
            throw new NException(NException.SERVICE_LOADDATA_ATTACKNUM_ERROR);
        }

        //设置指定属性
        entity.setServerId(serverId);
        entity.setLevel(1);
        entity.setBlood(blood);
        entity.setDefense(defenceNum);
        entity.setX(x);
        entity.setCamp(camp);

        entity.init();

        //添加到实体管理器
        SEntityManager.getInstance().setEntity(entity.getEid(), entity);

        //添加到场景管理器
        SHomeModel.getInstance().addJoinEntity(entity);

        //添加到建筑全局管理器
        this.stBuildEntityMap.put(entity.getEid(), entity);

        return entity;
    }

    /**
     * 删除一个建筑
     */
    public void remove(int eid) {
        this.stBuildEntityMap.remove(eid);

        //场景管理器移除此实体
        SHomeModel.getInstance().removeJoinEntity(eid);

        //实体管理器内移除此实体
        SEntityManager.getInstance().removeEntity(eid);
    }

    /**
     * 返回一个建筑
     * @param eid
     * @return
     */
    public SBuildEntity get(int eid) {
        return this.stBuildEntityMap.get(eid);
    }

    /**
     * 返回建筑列表
     * @return
     */
    public List<SBuildEntity> getBuildList() {
        List<SBuildEntity> buildList = new ArrayList<SBuildEntity>();

        for (Map.Entry<Integer, SBuildEntity> en : stBuildEntityMap.entrySet()) {
            buildList.add(en.getValue());
        }
        return buildList;
    }

    /**
     * 房间初始化调用的接口
     */
    public static void homeInit() {
//        //添加我方建筑
//        if (mapConfig.getCastleId() > 0 && mapConfig.getCastleHp() > 0) {
//            buildManager.add(mapConfig.getCastleId(), SMapEntity.MAP_LEFT_BORDER, mapConfig.getCastleHp(), mapConfig.getCastleDef(), EntityCampEnum.CAMP_LEFT);
//        }
//
//        //添加敌方建筑
//        if (mapConfig.getEnemyCastleId() > 0 && mapConfig.getEnemyCastleHp() > 0) {
//            int x = SHomeModel.getInstance().getMapEntity().getWidth() - SMapEntity.MAP_RIGHT_BORDER;
//            buildManager.add(mapConfig.getEnemyCastleId(), x , mapConfig.getEnemyCastleHp(), mapConfig.getEnemyCastleDef(), EntityCampEnum.CAMP_RIGHT);
//        }
    }
}