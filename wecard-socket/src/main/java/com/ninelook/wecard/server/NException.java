package com.ninelook.wecard.server;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.protocol.Response;
import io.netty.channel.Channel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 异常类
 * User: Simon
 * Date: 13-8-26 PM3:45
 */
public class NException extends RuntimeException {
    static Logger logger = LogManager.getLogger(NException.class.getName());

    /**
     * 错误号 - 参数不存在
     */
    public static final int ERROR_PARAM_NOT_EXISTS = 201;

    /**
     * 错误号 - 调用的方法不存在
     */
    public static final int ERROR_METHOD_NOT_EXISTS = 202;

    /**
     * 错误号 - 参数没有经过效验
     */
    public static final int ERROR_PARAM_VALID_FAIL = 203;

    /**
     * 错误号 - 未捕捉到的异常(语法错误或运行时错误)
     */
    public static final int ERROR_UNCATCH_EXCEPTION = 204;

    /**
     * 错误号 - UID验证失败
     */
    public static final int ERROR_UID_VALID_FAIL = 205;

    /**
     * 系统错误 - 消息 - 指定消息格式错误
     */
    public static final int SYSTEM_MESSAGE_ID_ERROR = 301;

    /**
     * 系统错误 - ASYNC - 当前消息不存在handler
     */
    public static final int ASYNC_NOT_HAVE_HANDLER = 1001;

    /**
     * 应用级错误 - 房间 - 已经拥有房间
     */
    public static final int SCENE_HOME_USER_ALREADY_HAVE_HOME = 10001;

    /**
     * 应用级错误 - 房间 - 当前房间不存在
     */
    public static final int SCENE_HOME_NOT_EXISTS = 10002;

    /**
     * 应用级错误 - 房间 - 当前用户已经加入房间
     */
    public static final int SCENE_HOME_USER_ALREADY_JOIN = 10003;

    /**
     * 应用级错误 - 房间 - 不是房主
     */
    public static final int SCENE_HOME_OWNER_ACCORD = 10004;

    /**
     * 应用级错误 - 房间 - 房间人数不够
     */
    public static final int SCENE_HOME_PLAYER_LIMIT_MIN_NUM = 10005;

    /**
     * 应用级错误 - 房间 - 房间ID错误
     */
    public static final int SCENE_HOME_ID_ERROR = 10006;

    /**
     * 应用级错误 - 房间 - 当前房间没有此用户
     */
    public static final int SCENE_HOME_PLAYER_NOT_EXISTS = 10007;

    /**
     * 应用级错误 - 房间 - 当前房间已经初始化
     */
    public static final int SCENE_HOME_ALREADY_INIT = 10008;

    /**
     * 应用级错误 - 房间 - 当前用户没有加入任何房间
     */
    public static final int SCENE_HOME_PLAYER_JOIN_NOTHING = 10009;

    /**
     * 应用级错误 - 房间 - 当前房间的状态不允许加入玩家
     */
    public static final int SCENE_HOME_JOIN_STATUS_ERROR = 10010;

    /**
     * 应用级错误 - 房间 - 当前房间的状态不允许调用此接口
     */
    public static final int SCENE_HOME_LOADING_ILLEGAL = 10011;

    /**
     * 应用级错误 - 房间 - 当前homeId已经存在
     */
    public static final int SCENE_HOME_MAP_HOMEID_EXISTS = 10012;

    /**
     * 应用级错误 - 房间 - 关闭房间出错, 房间内还有玩家存在
     */
    public static final int SCENE_HOME_PLAYER_EXISTS = 10013;

    /**
     * 应用级错误 - 房间 - 房间内没有玩家
     */
    public static final int SCENE_HOME_PLAYER_EMPTY = 10014;

    /**
     * 应用级错误 - 房间 - 房间状态不正确
     */
    public static final int SCENE_HOME_STATUS_ERROR = 10015;

    /**
     * 应用级错误 - 战斗 - 当前实体已经死亡
     */
    public static final int SERVICE_FIGHT_ALREADY_DIED = 10100;


    /**
     * 应用级错误 - 战斗 - 血量信息不正确
     */
    public static final int SERVICE_FIGHT_DEINCREMENT_BLOOD_ERROR = 10101;

    /**
     * 应用级错误 - 战斗 - 此次攻击序列非法
     */
    public static final int SERVICE_FIGHT_ILLEGALITY_SERIAL = 10102;

    /**
     * 应用级错误 - 战斗 - 重复的防御者
     */
    public static final int SERVICE_FIGHT_DUPLICATE_CASTER = 10103;

    /**
     * 应用级错误 - 战斗 - 当前实体没有普通攻击
     */
    public static final int SERVICE_FIGHT_NOT_WEAPON = 10104;

    /**
     * 应用级错误 - 战斗 - 攻击间隔时间太短
     */
    public static final int SERVICE_FIGHT_ATTACK_INTERVAL_TOO_SHORT = 10105;

    /**
     * 应用级错误 - 战斗实体 - 战斗实体没有死亡
     */
    public static final int SERVICE_ENTITY_NOT_DEAD = 10203;

    /**
     * 应用级错误 - 读取数据 - 血量不正确
     */
    public static final int SERVICE_LOADDATA_BLOOD_ERROR = 10304;

    /**
     * 应用级错误 - 读取数据 - 移动速度不正确
     */
    public static final int SERVICE_LOADDATA_SPEED_ERROR = 10305;

    /**
     * 应用级错误 - 读取数据 - 攻击力不正确
     */
    public static final int SERVICE_LOADDATA_ATTACKNUM_ERROR = 10306;

    /**
     * 应用级错误 - 技能 - 技能ID不正确
     */
    public static final int SERVICE_SKILL_SKILLID_ERROR = 10400;

    /**
     * 应用级错误 - 技能 - 技能type不正确
     */
    public static final int SERVICE_SKILL_SKILLTYPE_ERROR = 10401;

    /**
     * 应用级错误 - 技能 - 非英雄无法使用召唤技
     */
    public static final int SERVICE_SKILL_CAN_NOT_USE_CALL_UP_SKILL = 10402;

    public NException(int code) {
        super(String.valueOf(code));
    }

    /**
     * 捕获错误并记录
     *
     * @param uid
     * @param funName
     * @param code
     * @param e
     * @param ch
     */
    public static void catchErr(long uid, String funName, Integer code, Exception e, Channel ch) {
        logger.error(funName + " ... " + e.toString());
        e.printStackTrace();
        Response.HeshResMessage.Builder heshResMessage = ProtobufCoreHelper.getHeshResMessage();
        heshResMessage.setCode(code);

        //如果是用户发送的信息则推送错误信息
        if (ch != null) {
            ch.write(heshResMessage.build());
        }
    }
}
