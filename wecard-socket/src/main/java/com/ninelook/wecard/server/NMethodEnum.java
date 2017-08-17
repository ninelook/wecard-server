package com.ninelook.wecard.server;

/**
 * 消息方法常量
 * User: Ron
 */
public enum NMethodEnum {

    /**
     * 场景层 - 用户登陆
     */
    METHOD_USER_LOGIN("user.login"),

    /**
     * 场景层 - 用户离线
     */
    METHOD_USER_OFFLINE("user.offline"),

    /**
     * 场景层 - 系统复位
     */
    METHOD_SYSTEM_RESET("system.reset"),

    /**
     * 通知 - 通知用户
     */
    METHOD_NOTICE_PUSH("notice.push"),

    /**
     * 通知 - 推送信息 - 通知用户
     */
    PUSH_NOTICE_PUSH("notice.push.push"),

    /**
     * 聊天 - 发送一条消息
     */
    METHOD_CHAT_SEND("chat.send"),

    /**
     * 聊天 - 推送一条接收端消息
     */
        PUSH_CHAT_RECV("chat.recv.push"),

    /**
     * 聊天 - 公众号发送一条消息到个人
     */
    METHOD_CHAT_PUBTOPER("chat.PubToPer"),

    /**
     * 聊天 - 公众号发送一条消息到所有关注者
     */
    METHOD_CHAT_PUBTOALL("chat.PubToAll"),

    /**
     * 聊天 - 个人发送一条消息到公众号
     */
    METHOD_CHAT_PERTOPUB("chat.PerToPub"),

    /**
     * 聊天 - 公众号添加若干关注者
     */
    METHOD_CHAT_ADDSUB("chat.AddSub"),

    /**
     * 聊天 - 公众号删除若干关注者
     */
    METHOD_CHAT_DELSUB("chat.DelSub"),

    /**
     * 聊天 - 更新公众号自动回复信息
     */
    METHOD_CHAT_UPDATEREPLY("chat.UpdateReply");




    private String methodName;

    /**
     * 是否为当前方法
     * @param methodName
     * @return
     */
    public boolean isSame(String methodName) {
        return this.methodName.equals(methodName);
    }

    @Override
    public String toString() {
        return methodName;
    }

    NMethodEnum(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 通过一个字符串名称获取一个方法枚举
     * @param method
     * @return
     */
    public static NMethodEnum getMethodEnumByString(String method) {
        NMethodEnum findMethodEnum = null;
        for (NMethodEnum e : NMethodEnum.values()) {
            if (e.toString().equals(method)) {
                findMethodEnum = e;
            }
        }

        return findMethodEnum;
    }
}
