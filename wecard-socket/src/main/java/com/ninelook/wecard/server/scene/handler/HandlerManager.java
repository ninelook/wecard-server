package com.ninelook.wecard.server.scene.handler;

import com.ninelook.wecard.server.NContext;

/**
 * Handler管理器
 * User: Simon
 * Date: 13-12-25 PM10:42
 */
public class HandlerManager {

    /**
     * 返回指定mid的Handle
     *
     * @param mid
     * @return
     */
    public static Handler getHandler(int mid) {
        //TODO:优化为配置文件
        Handler handler = (Handler) NContext.getActx().getBean("Handler_" + mid);
        return handler;
    }
}
