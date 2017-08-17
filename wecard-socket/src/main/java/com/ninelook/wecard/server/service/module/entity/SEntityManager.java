package com.ninelook.wecard.server.service.module.entity;

import com.ninelook.wecard.server.service.common.model.SAbstractModel;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体管理器
 * User: Simon
 * Date: 2/12/14 18:21
 */
public class SEntityManager extends SAbstractModel {
    static Logger logger = LogManager.getLogger(SEntityManager.class.getName());

    /**
     * entity存储容器
     */
    Map<Integer, SEntity> stEntityMap = new HashMap<Integer, SEntity>();

    public static SEntityManager getInstance() {
        long commonUid = SAbstractModel.COMMON_UID;
        SEntityManager model = SContainerManager.getInstance().get(commonUid, SEntityManager.class);
        if (model == null) {
            model = new SEntityManager();
            SContainerManager.getInstance().set(commonUid, model);
        }
        return model;
    }

    /**
     * 构造函数
     */
    private SEntityManager() {
        super(SAbstractModel.COMMON_UID);
    }

    /**
     * 设置一个实体
     * @param eid
     * @param entity
     * @return
     */
    public boolean setEntity(int eid, SEntity entity) {
        stEntityMap.put(eid, entity);
        return true;
    }

    /**
     * 返回一个实体
     * @param eid
     * @return
     */
    public SEntity getEntity(int eid) {
        return stEntityMap.get(eid);
    }

    /**
     * 移除一个野怪
     * @param eid
     * @return
     */
    public boolean removeEntity(int eid) {
        stEntityMap.remove(eid);
        return true;
    }

}
