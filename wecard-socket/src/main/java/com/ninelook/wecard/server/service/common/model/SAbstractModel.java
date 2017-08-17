package com.ninelook.wecard.server.service.common.model;

import com.ninelook.wecard.server.NException;

import java.util.Map;

/**
 * 抽象底层model类
 * User: Simon
 * Date: 14-1-4
 */public abstract class SAbstractModel {

        /**
     * 当前模块用户ID
     */
    protected long uid;

    /**
     * 当前模块联合ID
     * 一个用户拥有多个当前模块时索引使用, 默认为0.
     */
    protected long unionId = 0;

    /**
     * 数据
     */
    protected Map data;

    public static final long COMMON_UID = 1;

    public SAbstractModel(long uid) {
        if (uid <= 0) {
            throw new NException(NException.ERROR_PARAM_VALID_FAIL);
        }
        this.uid = uid;

        //初始化
        init();
    }

    public SAbstractModel(long uid, long unionId) {
        if (uid <= 0 || unionId <= 0) {
            throw new NException(NException.ERROR_PARAM_VALID_FAIL);
        }
        this.uid = uid;
        this.unionId = unionId;

        //初始化
        init();
    }

    /**
     * 初始化当前模块
     */
    protected void init() {
    }

    /**
     * 获取用户ID
     * @return
     */
    public long getUid() {
        return uid;
    }

    /**
     * 返回联合ID
     * @return
     */
    public long getUnionId() {
        return unionId;
    }

    /**
     * 获取数据
     * @return
     */
    public Map getData() {
        return data;
    }

    /**
     * 保存数据
     * @return boolean
     */
    public boolean save() {
        //临时处理的数据临时存储.  最后保存,  以便加入异常处理机制
        return true;
    }

}
