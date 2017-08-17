package com.ninelook.wecard.server.scene.message;

import com.ninelook.wecard.server.NException;

/**
 * 消息ID类
 * User: Simon
 * Date: 13-12-25 PM4:22
 */
public class NMessageId {
    /**
     * 消息ID
     */
    private int id;

    /**
     * 消息模块ID
     */
    private int modId;

    /**
     * 消息控制器ID
     */
    private int conId;

    /**
     * 消息动作ID
     */
    private int actId;

    /**
     * 消息ID必须的长度
     */
    static final int MESSAGE_ID_LENGTH = 8;

    /**
     * 解析并返回消息ID类
     * @param mid
     * @return
     */
    public static NMessageId parse(int mid) throws NException {
        return new NMessageId(String.valueOf(mid));
    }

    private NMessageId(String mid) throws NException {
        if (mid.length() != MESSAGE_ID_LENGTH) {
            throw new NException(NException.SYSTEM_MESSAGE_ID_ERROR);
        }

        this.modId = Integer.valueOf(mid.substring(0, 2));
        if (this.modId <= 0) {
            throw new NException(NException.SYSTEM_MESSAGE_ID_ERROR);
        }

        this.conId = Integer.valueOf(mid.substring(2, 5));
        if (this.conId <= 0) {
            throw new NException(NException.SYSTEM_MESSAGE_ID_ERROR);
        }

        this.actId = Integer.valueOf(mid.substring(5, 8));
        if (this.actId <= 0) {
            throw new NException(NException.SYSTEM_MESSAGE_ID_ERROR);
        }

        Integer iId = Integer.valueOf(mid);
        if (iId.toString().length() != MESSAGE_ID_LENGTH) {
            throw new NException(NException.SYSTEM_MESSAGE_ID_ERROR);
        }

        this.id = iId;
    }

    public int getId() {
        return id;
    }

    public int getModId() {
        return modId;
    }

    public int getConId() {
        return conId;
    }


    public int getActId() {
        return actId;
    }
}
