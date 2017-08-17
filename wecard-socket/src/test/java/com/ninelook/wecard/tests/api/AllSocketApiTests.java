package com.ninelook.wecard.tests.api;

import com.ninelook.wecard.library.junit.HecTestCase;
import com.ninelook.wecard.library.net.WebSocketClient;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Map;

/**
* 所有测试
* User: Simon
* Date: 13-7-1 下午4:49
*/
public class AllSocketApiTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());

        //关闭所有Socket
        for (Map.Entry<Long, WebSocketClient> en : HecTestCase.socketClientsMap.entrySet()) {
            //暂不关闭
//            en.getValue().close();
        }
    }

    /**
     * 测试集合(保证按序执行)
     * @return
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Api Test");
        suite.addTest(ApiSystemTest.suite());
        suite.addTest(ApiSceneTest.suite());
        suite.addTest(ApiHomeTest.suite());

        return suite;
    }
}