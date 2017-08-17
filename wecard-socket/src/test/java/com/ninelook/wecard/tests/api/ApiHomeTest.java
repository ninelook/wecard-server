package com.ninelook.wecard.tests.api;

import com.ninelook.wecard.common.protobuf.ProtobufCoreHelper;
import com.ninelook.wecard.library.common.IdManager;
import com.ninelook.wecard.library.common.TestUtil;
import com.ninelook.wecard.library.junit.HecTestCase;
import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.protocol.Response;
import com.ninelook.wecard.protocol.apis.ApiHomeMessage;
import com.ninelook.wecard.protocol.beans.BeanHomeMessage;
import com.ninelook.wecard.server.NMidConstant;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 用户测试
 */
public class ApiHomeTest extends HecTestCase {
    public ApiHomeTest(String methodName) {
        super(methodName);
    }

    /**
     * 测试建立房间
     * @throws Exception
     */
    public void testCreateHome() throws Exception {
        ApiHomeMessage.ReqHomeCreate.Builder reqSceneCreateBuilder = ApiHomeMessage.ReqHomeCreate.newBuilder();
        reqSceneCreateBuilder.setMapId(IdManager.getInstance().mapIds.get(0));

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqHomeCreate(reqSceneCreateBuilder);

        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(0));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_HOME_CREATE);

        getSocket(IdManager.getInstance().uids.get(0)).write(reqMessageBuilder.build());

        Response.HeshResMessage msg = getSocket(IdManager.getInstance().uids.get(0)).read();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("返回房间用户ID不正确", (long)IdManager.getInstance().uids.get(0) ,msg.getHomeInfo().getOwnerUid());
    }

    /**
     * 测试加入房间
     * @throws Exception
     */
    public void testJoinHome() throws Exception {
        ApiHomeMessage.ReqHomeJoin.Builder req = ApiHomeMessage.ReqHomeJoin.newBuilder();
        req.setHomeId(1);

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqHomeJoin(req);


        ////接收房间内第二个用户的信息
        Long uid2 = IdManager.getInstance().uids.get(1);

        reqMessageBuilder.setUid(uid2);
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_HOME_JOIN);

        getSocket(uid2).write(reqMessageBuilder.build());

        Response.HeshResMessage msg2 = getSocket(uid2).read();

        //测试返回码
        assertEquals("code error", 0, msg2.getCode());
        assertEquals("返回房间玩家数量不正确", 2 ,msg2.getHomeInfo().getJoinPlayerListList().size());

        //接收房间内第一个用户的信息
        Long uid1 = IdManager.getInstance().uids.get(0);
        Response.HeshResMessage msg1 = getSocket(uid1).read();

        assertEquals("code error", 0, msg1.getCode());
        assertEquals("返回房间玩家数量不正确", 2, msg1.getHomeInfo().getJoinPlayerListList().size());

    }

    /**
     * 测试加入房间
     * @throws Exception
     */
    public void testOtherJoinHome() throws Exception {
        ApiHomeMessage.ReqHomeJoin.Builder req = ApiHomeMessage.ReqHomeJoin.newBuilder();
        req.setHomeId(1);

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqHomeJoin(req);

        ////接收房间内第二个用户的信息
        Long uid3 = IdManager.getInstance().uids.get(2);

        reqMessageBuilder.setUid(uid3);
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_HOME_JOIN);

        getSocket(uid3).write(reqMessageBuilder.build());

        Response.HeshResMessage msg3 = getSocket(uid3).read();

        //测试返回码
        assertEquals("code error", 0, msg3.getCode());
        assertEquals("返回房间玩家数量不正确", 3 ,msg3.getHomeInfo().getJoinPlayerListList().size());

        //接收房间内第一个用户的信息
        Long uid1 = IdManager.getInstance().uids.get(0);
        Response.HeshResMessage msg1 = getSocket(uid1).read();

        assertEquals("code error", 0, msg1.getCode());
        assertEquals("返回房间玩家数量不正确", 3, msg1.getHomeInfo().getJoinPlayerListList().size());

        //接收房间内第二个用户的信息
        Long uid2 = IdManager.getInstance().uids.get(1);
        Response.HeshResMessage msg2 = getSocket(uid2).read();

        assertEquals("code error", 0, msg2.getCode());
        assertEquals("返回房间玩家数量不正确", 3, msg2.getHomeInfo().getJoinPlayerListList().size());
    }

    /**
     * 测试退出房间
     * @throws Exception
     */
    public void testExitHome() throws Exception {
        ApiHomeMessage.ReqHomeExit.Builder req = ApiHomeMessage.ReqHomeExit.newBuilder();
        req.setHomeId(1);

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqHomeExit(req);


        ////接收房间内第二个用户的信息
        Long uid2 = IdManager.getInstance().uids.get(1);

        reqMessageBuilder.setUid(uid2);
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_EXIT_HOME);

        getSocket(uid2).write(reqMessageBuilder.build());

        Response.HeshResMessage msg2 = getSocket(uid2).read();

        //测试返回码
        assertEquals("code error", 0, msg2.getCode());
        assertEquals("offline status error", false, msg2.getHomeExitInfo().getOffline());


        //接收房间内第一个用户的信息
        Long uid1 = IdManager.getInstance().uids.get(0);
        getSocket(uid1).read();

        testJoinHome();
        testOtherJoinHome();
    }

    /**
     * 测试获取用户基本信息
     * @throws Exception
     */
    public void testGetUserInfo() throws Exception {
        ApiHomeMessage.ReqGetUserInfo.Builder req = ApiHomeMessage.ReqGetUserInfo.newBuilder();
        req.setHomeId(1);
        req.addLUid(IdManager.getInstance().uids.get(0));
        req.addLUid(IdManager.getInstance().uids.get(1));
        req.addLUid(IdManager.getInstance().uids.get(2));

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqGetUserInfo(req);

        ////接收房间内第二个用户的信息
        Long uid1 = IdManager.getInstance().uids.get(0);

        reqMessageBuilder.setUid(uid1);
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_GET_USERINFO);

        getSocket(uid1).write(reqMessageBuilder.build());

        Response.HeshResMessage msg1 = getSocket(uid1).read();

        //测试返回码
        assertEquals("code error", 0, msg1.getCode());
        assertEquals("返回房间玩家数量不正确", 3 ,msg1.getLUserInfoCount());
    }

    /**
     * 测试房间开始
     * @throws Exception
     */
    public void testGoHome() throws Exception {
        ApiHomeMessage.ReqHomeGo.Builder req = ApiHomeMessage.ReqHomeGo.newBuilder();
        req.setHomeId(1);

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqHomeGo(req);

        Long uid = IdManager.getInstance().uids.get(0);

        reqMessageBuilder.setUid(uid);
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_HOME_GO);

        getSocket(uid).write(reqMessageBuilder.build());

        Response.HeshResMessage msg = getSocket(uid).read();

        //广播消息, 接收第二位玩家的信息
        getSocket(IdManager.getInstance().uids.get(1)).read();

        //广播消息, 接收第三位玩家的信息
        getSocket(IdManager.getInstance().uids.get(2)).read();

        BeanHomeMessage.WarInfo warInfo = msg.getWarInfo();

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("英雄上场数量不正确", 3, warInfo.getHeroInfoListCount());
        assertTrue("地图信息不正确", warInfo.getMapInfo().getWidth() > 0);
    }

    /**
     * 测试load加载结束, 最后一个完成后则真正进入房间
     * @throws Exception
     */
    public void testLoadingFinish() throws Exception {
        Communication.HeshReqMessage.Builder reqMessageBuilder;

        ApiHomeMessage.ReqLoadingFinish.Builder req = ApiHomeMessage.ReqLoadingFinish.newBuilder();
        req.setHomeId(1);

        long uid1 = IdManager.getInstance().uids.get(0);
        long uid2 = IdManager.getInstance().uids.get(1);
        long uid3 = IdManager.getInstance().uids.get(2);

        //TEST:第一位玩家加载完成
        reqMessageBuilder = ProtobufCoreHelper.getHeshReqBySceneMessage(NMidConstant.MESSAGE_ID_SCENE_LOADING_FINISH, uid1);
        req.addLUid(uid1);
        reqMessageBuilder.getSceneReqMessageBuilder().setReqLoadingFinish(req);

        getSocket(uid1).write(reqMessageBuilder.build());
        Response.HeshResMessage msg1 = getSocket(uid1).read();

        //TEST:第二位玩家加载完成
        reqMessageBuilder = ProtobufCoreHelper.getHeshReqBySceneMessage(NMidConstant.MESSAGE_ID_SCENE_LOADING_FINISH, uid2);

        req.clearLUid();
        req.addLUid(uid2);
        reqMessageBuilder.getSceneReqMessageBuilder().setReqLoadingFinish(req);

        getSocket(uid2).write(reqMessageBuilder.build());
        Response.HeshResMessage msg2 = getSocket(uid2).read();

        //TEST:第三位玩家加载完成
        reqMessageBuilder = ProtobufCoreHelper.getHeshReqBySceneMessage(NMidConstant.MESSAGE_ID_SCENE_LOADING_FINISH, uid3);

        req.clearLUid();
        req.addLUid(uid3);
        reqMessageBuilder.getSceneReqMessageBuilder().setReqLoadingFinish(req);

        getSocket(uid3).write(reqMessageBuilder.build());
        Response.HeshResMessage msg3 = getSocket(uid3).read();

        int i = 0;
        while (i < 3) {
            getSocket(uid1).read();
            getSocket(uid2).read();
            msg3 = getSocket(uid3).read();
            i ++;
        }

        BeanHomeMessage.HomeGoInfo homeGoInfo = msg3.getHomeGoInfo();

        //测试返回码
        assertEquals("code error", 0, msg3.getCode());
        assertEquals("玩家数量不正确", 3, homeGoInfo.getLPlayerInfoCount());
    }

    /**
     * 获取指定地图内的房间列表
     * @throws Exception
     */
    public void testGetHomeList() throws Exception {
        ApiHomeMessage.ReqGetList.Builder reqSceneGetHomeListBuilder = ApiHomeMessage.ReqGetList.newBuilder();
        reqSceneGetHomeListBuilder.setMapId(IdManager.getInstance().mapIds.get(0));

        Communication.HeshReqMessage.Builder reqMessageBuilder = TestUtil.getReqSceneMessage();
        reqMessageBuilder.getSceneReqMessageBuilder().setReqGetList(reqSceneGetHomeListBuilder);

        reqMessageBuilder.setUid(IdManager.getInstance().uids.get(0));
        reqMessageBuilder.setMid(NMidConstant.MESSAGE_ID_SCENE_GET_HOME_LIST);

        getSocket(IdManager.getInstance().uids.get(0)).write(reqMessageBuilder.build());

        Response.HeshResMessage msg = getSocket(IdManager.getInstance().uids.get(0)).read();

        while (!msg.hasMapHomeListInfo()) {
            msg = getSocket(IdManager.getInstance().uids.get(0)).read();
        }

        //测试返回码
        assertEquals("code error", 0, msg.getCode());
        assertEquals("房间列表信息不正确", 1 ,msg.getMapHomeListInfo().getLMapHomeInfoCount());
    }

    /**
     * 测试集合(保证按序执行)
     * @return
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("HomeTest");
        suite.addTest(new ApiHomeTest("testCreateHome"));
        suite.addTest(new ApiHomeTest("testJoinHome"));
        suite.addTest(new ApiHomeTest("testExitHome"));
        suite.addTest(new ApiHomeTest("testGetUserInfo"));
        suite.addTest(new ApiHomeTest("testGoHome"));
        suite.addTest(new ApiHomeTest("testLoadingFinish"));
        suite.addTest(new ApiHomeTest("testGetHomeList"));
        return suite;
    }
}