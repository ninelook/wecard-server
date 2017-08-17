package com.ninelook.wecard.server.service.common.model.container;

import com.ninelook.wecard.server.service.common.model.SAbstractModel;

/**
 * 容器的model信息
 * User: Simon
 * Date: 13-7-31 PM5:37
 */
public class SModelInfo {
    /**
     * 模块uid
     */
    private long uid;

    /**
     * 模块联合ID
     */
    private long unionId;

    /**
     * 模块对象
     */
    private SAbstractModel model;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUnionId() {
        return unionId;
    }

    public void setUnionId(long unionId) {
        this.unionId = unionId;
    }

    public SAbstractModel getModel() {
        return model;
    }

    public void setModel(SAbstractModel model) {
        this.model = model;
    }
}
