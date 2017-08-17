package com.ninelook.wecard.server.gateway.auth;

/**
 * 用户会话
 *
 * User: Simon
 * Date: 13-12-26 AM12:46
 */
public class Session {
    private long uid;

    private String sessionKey;

    private boolean isValid = false;

    private int loginTime;

    /**
     * 验证权限
     * @return
     */
    public boolean authValid() {

        //权限验证
        this.isValid = true;
        return true;
    }

    /**
     * 获取用户ID
     * @return
     */
    public long getUid() {
        return uid;
    }

    /**
     * 获取sessionKey
     * @return
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 当前session验证是否通过
     * @return
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * 获取登录时间
     * @return
     */
    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
