package com.ninelook.wecard.server.scene.player;

/**
 * 实体所处阵营
 * User: Simon
 * Date: 14-1-5 下午2:50
 */
public enum EntityCampEnum {
    /**
     * 未选择阵营
     */
    CAMP_UNITIT(0),

    /**
     * 左方
     */
    CAMP_LEFT(1),

    /**
     * 右方
     */
    CAMP_RIGHT(2);

    private int index;

    private EntityCampEnum(int index) {
        this.index = index;
    }

    /**
     * 返回当前枚举的索引值
     * @return
     */
    public int getIndex() {
        return this.index;
    }
}
