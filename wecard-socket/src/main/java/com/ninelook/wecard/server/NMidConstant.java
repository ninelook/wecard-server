package com.ninelook.wecard.server;

/**
 * 消息ID常量
 * User: Simon
 * Date: 13-12-25 PM5:29
 */
public class NMidConstant {
    /**
     * 消息模块ID - 场景模块
     */
    public static final int MESSAGE_MOD_SCENE = 10;

    /**
     * 消息模块ID - 房间模块
     */
    public static final int MESSAGE_MOD_SERVICE = 11;

    /**
     * 场景层 - 系统级 - 登陆
     */
    public static final int MESSAGE_ID_SCENE_SYSTEM_LOGIN = 10100100;

    /**
     * 场景层 - 系统级 - 用户离线
     */
    public static final int MESSAGE_ID_SCENE_SYSTEM_OFFLINE = 10100101;

    /**
     * 场景层 - 系统级 - 测试复位
     */
    public static final int MESSAGE_ID_SCENE_SYSTEM_TEST_RESET = 10100102;

    /**
     * 场景层 - 房间 - 建立房间
     */
    public static final int MESSAGE_ID_SCENE_HOME_CREATE = 10200100;

    /**
     * 场景层 - 房间 - 加入房间
     */
    public static final int MESSAGE_ID_SCENE_HOME_JOIN = 10200101;

    /**
     * 场景层 - 房间 - 开始游戏
     */
    public static final int MESSAGE_ID_SCENE_HOME_GO = 10200102;

    /**
     * 场景层 - 房间 - 获取用户的基本信息
     */
    public static final int MESSAGE_ID_SCENE_GET_USERINFO = 10200103;

    /**
     * 场景层 - 房间 - 前台资源加载完毕
     */
    public static final int MESSAGE_ID_SCENE_LOADING_FINISH = 10200104;

    /**
     * 场景层 - 房间 - 获取地图内的房间列表
     */
    public static final int MESSAGE_ID_SCENE_GET_HOME_LIST = 10200105;

    /**
     * 场景层 - 房间 - 退出房间
     */
    public static final int MESSAGE_ID_SCENE_EXIT_HOME = 10200106;

    /**
     * 服务层 - 英雄 - 移动
     */
    public static final int MESSAGE_ID_SERVICE_HERO_MOVE = 11100200;

    /**
     * 服务层 - 战斗 - 攻击
     */
    public static final int MESSAGE_ID_SERVICE_FIGHT_ATTACK = 11100300;


    /**
     * 服务层 - 技能 - 使用技能
     */
    public static final int MESSAGE_ID_SERVICE_SKILL_USE = 11100400;
}
