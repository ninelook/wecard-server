package com.ninelook.wecard.library.junit;

import com.ninelook.wecard.common.config.ConfigHandler;
import com.ninelook.wecard.library.common.IdManager;
import com.ninelook.wecard.library.config.SystemConfigHandler;
import com.ninelook.wecard.library.net.WebSocketClient;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Simon
 * Date: 13-6-28 下午5:17
 */
public class HecTestCase extends TestCase {

    protected IdManager idManager = new IdManager();

    public HecTestCase() {
        super();
    }

    public HecTestCase(String name) {
        super(name);
    }

    public static Map<Long, WebSocketClient> socketClientsMap  = new HashMap<Long, WebSocketClient>();

    static {

        for (Long uid : IdManager.getInstance().uids) {
            WebSocketClient socketClient = new WebSocketClient(uid, SystemConfigHandler.getInstance().getServerUrl());

            try {
                socketClient.run();
            } catch (Exception e) {
                e.printStackTrace();
            }

            socketClientsMap.put(uid, socketClient);
        }

        try {
            Thread.sleep(1500);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //返回一个指定用户的socket连接
    protected WebSocketClient getSocket(long uid) {
        return socketClientsMap.get(uid);
    }
}
