//package com.ninelook.moko.tests.unit.friend;
//
//import com.davinci.library.common.IdManager;
//import com.davinci.mock.ModelsMock;
//import com.davinci.service.module.friend.entity.FriendEntity;
//import com.davinci.service.module.friend.model.FriendModel;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.util.Map;
//
///**
// * 好友测试类
// */
//public class FriendModelTest {
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//        ModelsMock.init();
//    }
//
//    /**
//     * 测试添加一个好友
//     * @throws Exception
//     */
//    @Test
//    public void testAddFriend() throws Exception {
//        FriendModel friendModel = FriendModel.getInstance(IdManager.getInstance().uids.get(0), true);
//
//        friendModel.addFriend(IdManager.getInstance().uids.get(1));
//
//        Map<Long, FriendEntity> friendList = friendModel.getStFriendList();
//
//        Assert.assertTrue(friendList.get(IdManager.getInstance().uids.get(1)) != null);
//    }
//}
