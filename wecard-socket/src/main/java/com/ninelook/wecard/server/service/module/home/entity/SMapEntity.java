package com.ninelook.wecard.server.service.module.home.entity;

/**
 * 地图信息
 * User: Simon
 * Date: 14-1-4 下午2:41
 */
public class SMapEntity {

    /**
     * 地图ID
     */
    private int mapId;

    /**
     * 地图宽度
     */
    private int width;

    /**
     * 单屏屏幕宽度
     */
    public static final int SCREEN_SINGLE_WIDTH = 1280;

    /**
     * 地图左边边距
     */
    public static final int MAP_LEFT_BORDER = 200;

    /**
     * 地图右边边距
     */
    public static final int MAP_RIGHT_BORDER = 200;

    public SMapEntity(int mapId) {
        this.mapId = mapId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width * SCREEN_SINGLE_WIDTH;
    }
}
