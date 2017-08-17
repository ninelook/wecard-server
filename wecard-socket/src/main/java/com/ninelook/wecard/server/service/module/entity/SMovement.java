package com.ninelook.wecard.server.service.module.entity;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.beans.BeanEntityMessage;
import com.ninelook.wecard.server.service.common.protobuf.SProtobufEntityHelper;
import com.ninelook.wecard.server.service.common.util.SGatewayHelper;
import com.ninelook.wecard.server.service.module.home.entity.SMapEntity;
import com.ninelook.wecard.server.service.module.home.model.SHomeModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 移动
 * User: Simon
 * Date: 3/21/14 10:46
 */
public class SMovement {
    static Logger logger = LogManager.getLogger(SMovement.class.getName());

    protected SUnit unit;

    /**
     * 每秒移动速度
     */
    protected float speedSec;

    /**
     * 移动方向
     */
    protected int dir;

    /**
     * 移动数据同步间隔时间
     */
    public static final int INTERVAL_TIME_MOVE_SYNC_DATA = 3000;

    private int syncMoveDataTimer = INTERVAL_TIME_MOVE_SYNC_DATA;

    /**
     * 移动方向 - 向后
     */
    public static final int MOVE_DIR_LEFT = -1;

    /**
     * 移动方向 - 停止
     */
    public static final int MOVE_DIR_STOP = 0;

    /**
     * 移动方向 - 向前
     */
    public static final int MOVE_DIR_RIGHT = 1;

    public SMovement(SUnit unit) {
        this.unit = unit;
    }

    public void setSpeedSec(float speedSec) {
        this.speedSec = speedSec;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        int srcDir = this.dir;
        this.dir = dir;

        //同步移动数据
        if (this.dir != srcDir) {
            syncMoveData();
        }
    }

    /**
     * 更新移动
     */
    public void moveUpdate(float diff) {
        if (dir != MOVE_DIR_STOP) {
            logger.info("Movement.moveUpdate ... [eid:" + unit.getEid() + ", dir:" + dir + ", x:" + unit.getX());
            int startX = (int)unit.getX();
            float x = unit.getX() + (diff * (speedSec / 1000)) * dir;

            unit.setX(x);

            //不得超出右边边界
            if (unit.getX() > SHomeModel.getInstance().getMapEntity().getWidth()) {
                unit.setX(SHomeModel.getInstance().getMapEntity().getWidth());

                setDir(MOVE_DIR_STOP);

                return;
            }

            //不得超出左边边界
            if (unit.getX() < SMapEntity.MAP_LEFT_BORDER) {
                unit.setX(SMapEntity.MAP_LEFT_BORDER);

                setDir(MOVE_DIR_STOP);

                return;
            }

            //同步移动数据到客户端
            if (syncMoveDataTimer <= diff) {
                syncMoveData();
                syncMoveDataTimer = INTERVAL_TIME_MOVE_SYNC_DATA;
            } else syncMoveDataTimer -= diff;
        }
    }

    /**
     * 向客户端同步数据
     */
    public void syncMoveData() {
        logger.info("Movement.moveUpdate ... sync move data[eid:" + unit.getEid() +", x:" + unit.getX() + unit.getServerId());

        //针对当前实体移动信息进行广播
        BeanEntityMessage.PosInfo.Builder heroPosInfoBuilder = SProtobufEntityHelper.getPosInfo(unit.getEid());

        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setHeroPosInfo(heroPosInfoBuilder);

        SGatewayHelper.sendMessageToHome(heshResMessage.build());
    }
}
