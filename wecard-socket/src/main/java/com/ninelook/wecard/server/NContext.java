package com.ninelook.wecard.server;

/**
 * 全局信息容器
 *
 * User: Simon
 * Date: 13-12-15 PM9:09
 */

import org.springframework.context.ApplicationContext;

/**
 * 通用常量列表
 */
public class NContext {
    private static ApplicationContext applicationContext;

    /**
     * 开发环境
     */
    private static final int DEV_ENV = 1;

    /**
     * 当前使用的环境
     */
    private static int ENV = DEV_ENV;

    /**
     * 设置Spring
     * @param cxt
     */
    public static void setApplicationContext(ApplicationContext cxt) {
        applicationContext = cxt;
    }

    /**
     * 获取Spring
     */
    public static ApplicationContext getActx() {
        return applicationContext;
    }

    public static boolean isDevEnv() {
        return ENV == DEV_ENV;
    }

}
