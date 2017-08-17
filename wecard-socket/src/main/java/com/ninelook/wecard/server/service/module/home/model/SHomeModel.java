package com.ninelook.wecard.server.service.module.home.model;

import com.ninelook.wecard.server.service.common.model.SAbstractModel;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import com.ninelook.wecard.server.service.module.entity.SUnit;
import com.ninelook.wecard.server.service.module.hero.SHeroManager;
import com.ninelook.wecard.server.service.module.home.entity.SMapEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间模块
 * User: Simon
 * Date: 14-1-3 下午4:55
 */
public class SHomeModel extends SAbstractModel {
    static Logger logger = LogManager.getLogger(SHomeModel.class.getName());

    /**
     * 房间ID
     */
    private int homeId;

    /**
     * 最后一次生成的eid
     */
    private int lastGlobalEid = 0;

    /**
     * 最后一次生成的序列ID
     */
    private int lastSid = 0;

    /**
     * 房间开始时间(毫秒)
     */
    private long startTime;

    /**
     * 战斗是否已经结束
     */
    private boolean isFinish = false;

    /**
     * 上一次tick执行时间
     */
    public long prevTickTime = 0;



    /**
     * 关联地图
     */
    private SMapEntity stMapEntity;

    /**
     * 加入的实体列表
     */
    private Map<Integer, SUnit> joinEntityMap = new HashMap<Integer, SUnit>();

    /**
     * 加入的玩家列表
     */
    private List<Long> joinPlayerList = new ArrayList<Long>();

    /**
     * 构造函数
     */
    private SHomeModel() {
        super(SAbstractModel.COMMON_UID);

        this.stMapEntity = new SMapEntity(1);
        this.stMapEntity.setWidth(10);
    }


    /**
     * 获取SHomeModel
     * @return
     */
    public static SHomeModel getInstance() {
        long commonUid = SAbstractModel.COMMON_UID;
        SHomeModel model = SContainerManager.getInstance().get(commonUid, SHomeModel.class);
        if (model == null) {
            model = new SHomeModel();
            SContainerManager.getInstance().set(commonUid, model);
        }
        return model;
    }

    public List<Long> getJoinPlayerList() {
        return joinPlayerList;
    }

    public void addPlayer(long uid) {
        this.joinPlayerList.add(uid);
    }

    /**
     * 批量加入战斗实体
     * @return
     */
    public void addJoinEntity(SUnit entity) {
        this.joinEntityMap.put(entity.getEid(), entity);
    }

    /**
     * 删除一个加入的战斗实体
     * @return
     */
    public void removeJoinEntity(int eid) {
        this.joinEntityMap.remove(eid);
    }

    /**
     * 返回加入此房间的玩家列表
     * @return
     */
    public Map<Integer, SUnit> getJoinEntityMap() {
        return joinEntityMap;
    }

    /**
     * 获取当前房间地图信息
     * @return
     */
    public SMapEntity getMapEntity() {
        return stMapEntity;
    }

    /**
     * 返回一个新的实体唯一ID
     */
    public int getNewEid() {
        return ++lastGlobalEid;
    }

    /**
     * 返回最后一次生成的序列ID
     * @return
     */
    public int getLastSid() {
        return ++lastSid;
    }

    /**
     * 返回房间开始时间(毫秒)
     * @return
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 返回房间ID
     * @return
     */
    public int getHomeId() {
        return homeId;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean Finish) {
        isFinish = Finish;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 房间初始化调用的接口
     * @param homeId
     * @param mapId
     * @param joinPlayerList
     * @return
     */
    public boolean homeInit(int homeId, int mapId, List<Long> joinPlayerList) {
        this.homeId = homeId;

        ////////英雄初始化
        logger.info("SHomeModel.homeInit ... SHeroManager.homeInit");
        SHeroManager.homeInit(homeId, joinPlayerList);


        return true;
    }

    /**
     * 定时更新方法
     * @param diff
     */
    public void update(int diff) {
        //战斗已经结束则不进行刷新操作
        if (this.isFinish == true)
            return;

        ArrayList<SUnit> list = new ArrayList<SUnit>();
        for (SUnit unit : joinEntityMap.values()) {
            list.add(unit);
        }

        //遍历战场上实体并更新
        for (SUnit unit : list) {
            unit.update(diff);
        }
    }

    /**
     * 获取地图右边界座标
     * @return
     */
    public int getRightX() {
        int rightX = stMapEntity.getWidth() - SMapEntity.MAP_RIGHT_BORDER;

        return rightX;
    }

    /**
     * 获取地图左边界座标
     * @return
     */
    public int getLeftX() {
        return SMapEntity.MAP_LEFT_BORDER;
    }
}

