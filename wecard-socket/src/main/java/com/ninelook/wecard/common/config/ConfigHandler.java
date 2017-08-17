package com.ninelook.wecard.common.config;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置文件XML读取类
 * User: Ron
 */
public class ConfigHandler {

    protected static ConfigHandler systemConfigHandler = new ConfigHandler();

    /**
     * 当前配置根DOC
     */
    protected Document doc;

    /**
     * 单例模式返回
     * @return
     */
    public static ConfigHandler getInstance() {
        return systemConfigHandler;
    }

    public ConfigHandler() {
        try {
            //初始化配置
            SAXBuilder sax = new SAXBuilder();
            doc = sax.build("config.xml");

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取指定名称的Config配置
     * @return
     */
    public Map<String,String> getConfig(String name) {
        Element rootEle = doc.getRootElement();
        Element elements = rootEle.getChild(name);

        Map<String,String> configMap = new HashMap<String, String>();
        if (elements != null) {
            List<Element> childElementList = elements.getChildren();
            for(Element childElement : childElementList) {
                String lname = childElement.getName();
                String lvalue = childElement.getValue();

                configMap.put(lname, lvalue);
            }
        }

        return configMap;
    }

    /**
     * 返回socket服务器的端口号
     * @return
     */
    public int getSocketServerPort() {
        int port = 8087;
        Map<String,String> socketServerConfig = getConfig("socketServer");
        int portConfig = Integer.valueOf(socketServerConfig.get("port"));
        if (portConfig > 0) {
            port = portConfig;
        }

        return port;
    }


}

