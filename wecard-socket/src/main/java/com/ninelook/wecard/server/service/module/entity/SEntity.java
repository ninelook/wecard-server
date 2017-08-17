package com.ninelook.wecard.server.service.module.entity;

import com.ninelook.wecard.server.service.module.home.model.SHomeModel;

/**
 * 地图上的实体
 * User: Simon
 * Date: 14-1-20 下午3:59
 */
public class SEntity {
    protected int eid;

    protected float x;

    protected float y;

    protected SEntityTypeEnum type;

    public SEntity(SEntityTypeEnum type) {
        this.eid = SHomeModel.getInstance().getNewEid();

        this.type = type;
    }

    public int getEid() {
        return eid;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public SEntityTypeEnum getType() {
        return type;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SEntity) {
            SEntity entity = (SEntity)obj;
            return this.eid == entity.getEid();
        }
        return super.equals(obj);
    }
}
