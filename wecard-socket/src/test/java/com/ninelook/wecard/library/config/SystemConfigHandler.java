package com.ninelook.wecard.library.config;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;

/**
 * User: Simon
 * Date: 13-7-3 下午3:47
 */
public class SystemConfigHandler {

    protected static SystemConfigHandler systemConfigHandler = new SystemConfigHandler();

    /**
     * 当前配置根DOC
     */
    protected Document doc;

    /**
     * 单例模式返回
     * @return
     */
    public static SystemConfigHandler getInstance() {
        return systemConfigHandler;
    }

    public SystemConfigHandler() {
        try {
            //初始化配置
            SAXBuilder sax = new SAXBuilder();
            doc = sax.build("src/test/java/com/ninelook/wecard/config/SystemConfig.xml");

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取目标测试服务器URL
     * @return
     */
    public String getServerUrl() {
        Element rootEle = doc.getRootElement();
        Element serverUrlEle = rootEle.getChild("server-url");
        return serverUrlEle.getValue();
    }
}
