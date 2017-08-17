package com.ninelook.wecard.tests.api;

import com.ninelook.wecard.library.common.IdManager;
import com.ninelook.wecard.library.common.TestUtil;
import com.ninelook.wecard.library.junit.HecTestCase;
import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.apis.ApiSceneMessage;
import com.ninelook.wecard.server.NMidConstant;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
* 场景测试测试
*/
public class ApiSceneTest extends HecTestCase {
    public ApiSceneTest(String methodName) {
        super(methodName);
    }

    /**
     * 测试用户Login
     * @throws Exception
     */
    public void testLogin() throws Exception {
        ApiSceneMessage.ReqSceneLogin.Builder ReqSceneLoginBuilder = ApiSceneMessage.ReqSceneLogin.newBuilder();

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqSceneLogin(ReqSceneLoginBuilder);

        Response.HeshResMessage msg;

        /**
         * TEST - 第一个玩家登陆
         */
        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(0));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_SYSTEM_LOGIN);

        getSocket(IdManager.getInstance().uids.get(0)).write(reqMessageBuilder.build());
        msg = getSocket(IdManager.getInstance().uids.get(0)).read();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("第一个, 返回信息用户ID不正确", (long)IdManager.getInstance().uids.get(0) ,msg.getPlayerInfo().getUid());

        /**
         * TEST - 第二个玩家登陆
         */
        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(1));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_SYSTEM_LOGIN);

        getSocket(IdManager.getInstance().uids.get(1)).write(reqMessageBuilder.build());
        msg = getSocket(IdManager.getInstance().uids.get(1)).read();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("第二个, 返回信息用户ID不正确", (long)IdManager.getInstance().uids.get(1) ,msg.getPlayerInfo().getUid());


        /**
         * TEST - 第三个玩家登陆
         */
        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(2));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_SYSTEM_LOGIN);

        getSocket(IdManager.getInstance().uids.get(2)).write(reqMessageBuilder.build());
        msg = getSocket(IdManager.getInstance().uids.get(2)).read();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("第二个, 返回信息用户ID不正确", (long)IdManager.getInstance().uids.get(2) ,msg.getPlayerInfo().getUid());


        /**
         * TEST - 第四个玩家登陆
         */
        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(3));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_SYSTEM_LOGIN);

        getSocket(IdManager.getInstance().uids.get(3)).write(reqMessageBuilder.build());
        msg = getSocket(IdManager.getInstance().uids.get(3)).read();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("第二个, 返回信息用户ID不正确", (long)IdManager.getInstance().uids.get(3) ,msg.getPlayerInfo().getUid());
    }

    /**
     * 测试集合(保证按序执行)
     * @return
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("SceneTest");
        suite.addTest(new ApiSceneTest("testLogin"));
        return suite;
    }
}
