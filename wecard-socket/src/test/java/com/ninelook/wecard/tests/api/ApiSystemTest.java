package com.ninelook.wecard.tests.api;

import com.ninelook.wecard.library.common.IdManager;
import com.ninelook.wecard.library.common.TestUtil;
import com.ninelook.wecard.library.junit.HecTestCase;
import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.server.NMidConstant;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
* 场景测试测试
*/
public class ApiSystemTest extends HecTestCase {
    public ApiSystemTest(String methodName) {
        super(methodName);
    }

    /**
     * 清除数据
     * @throws Exception
     */
    public void testCleanAll() throws Exception {
        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();

        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(0));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_SYSTEM_TEST_RESET);

        getSocket(IdManager.getInstance().uids.get(0)).write(reqMessageBuilder.build());
    }

    /**
     * 测试集合(保证按序执行)
     * @return
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("SystemTest");
        suite.addTest(new ApiSystemTest("testCleanAll"));
        return suite;
    }
}
