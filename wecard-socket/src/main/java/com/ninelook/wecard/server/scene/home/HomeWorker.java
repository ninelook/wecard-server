package com.ninelook.wecard.server.scene.home;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.HandlerManager;
import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.player.Player;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import com.ninelook.wecard.server.scene.player.PlayerStatusEnum;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.common.model.container.SContainerManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetlang.channels.BatchSubscriber;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 房间核心工作类
 * User: Simon
 * Date: 13-12-17 PM6:31
 */
public class HomeWorker {
    static Logger logger = LogManager.getLogger(HomeWorker.class.getName());

    /**
     * 房间消息Channel
     */
    private final Channel<NMessage> messageChannel = new MemoryChannel<NMessage>();

    /**
     * fiber线程
     */
    private Fiber fiberThread;

    /**
     * 房间ID
     */
    private int homeId;

    /**
     * 房间关联的MapId
     */
    private int mapId;

    /**
     * 房间状态
     */
    private volatile HomeStatusEnum status = HomeStatusEnum.UNINIT;

    /**
     * 房间所属用户ID
     */
    private long ownerUid;

    /**
     * 所有加入的玩家
     */
    private List<Player> joinPlayersList = new ArrayList<Player>();

    /**
     * 房间最多可加入人数
     */
    public static int JOIN_PLAYER_LIMIT_MAX_NUM = 10;

    /**
     * 房间开始游戏最小人数
     */
    public static int GO_JOIN_PLAYER_LIMIT_MIN_NUM = 2;

    public HomeWorker(Fiber fiberThread, int mapId, int homeId, long ownerId) {
        this.fiberThread = fiberThread;
        this.mapId = mapId;
        this.homeId = homeId;
        this.ownerUid = ownerId;

        //回调方法
        Callback<List<NMessage>> callback = new HomeWorkerCallback(this);

        //像消息通道注册消息处理观察者
        BatchSubscriber<NMessage> batchSubscriber = new BatchSubscriber<NMessage>(this.fiberThread, callback, 10, TimeUnit.MILLISECONDS);
        this.messageChannel.subscribe(this.fiberThread, batchSubscriber);

        //改变房间状态为玩家准备中
        this.status = HomeStatusEnum.PLAYER_READY_DOING;
    }

    /**
     * 房间启动运转
     */
    public void start() {
        fiberThread.start();
    }

    /**
     * 向当前房间发送一条消息
     * @param message
     */
    public void sendMessage(NMessage message) {
        messageChannel.publish(message);
    }


    /**
     * 通道调用方法
     * @param message
     * @return
     */
    private void channelOnMessage(NMessage message) {
        try {
            long currTickTime = System.currentTimeMillis();
            String interfaceName = "";

            if (message instanceof MessageHaveHandler) { //消息具有Handler关联, 一般为内部接口
                int homeId = ((SServiceInnerMessage) message).getHomeId();
                if (homeId <= 0)
                    throw new NException(NException.ERROR_PARAM_NOT_EXISTS);
                SContainerManager.setCtxHomeId(homeId);

                MessageHaveHandler hMessage = (MessageHaveHandler)message;
                hMessage.exeHandler();

                interfaceName = hMessage.getClass().getName();

            } else { //对外公开接口调用, 一般包含protobuf的消息体
                int homeId = message.getHeshReqMessage().getServiceReqMessage().getHomeId();
                if (homeId <= 0)
                    throw new NException(NException.ERROR_PARAM_NOT_EXISTS);
                SContainerManager.setCtxHomeId(homeId);

                Handler h = HandlerManager.getHandler(message.getMid());
                h.handle(message);

                interfaceName = h.getClass().getName();
            }

            //执行时间监测
            long executeTime = System.currentTimeMillis() - currTickTime;

            //执行10毫秒以上接口视为危险接口
            if (executeTime > 1) {
                logger.error("Execute Time: " + executeTime + "ms, interface:" + interfaceName);
            }

        } catch (NException e) {
            NException.catchErr(message.getUid(), "HomeWorker.channelOnMessage",
                                        Integer.valueOf(e.getMessage()), e, message.getChannel());
        } catch (RuntimeException e) {
            NException.catchErr(message.getUid(), "HomeWorker.channelOnMessage",
                                        NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
        } catch (Exception e) {
            NException.catchErr(message.getUid(), "HomeWorker.channelOnMessage",
                                        NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
        }
    }

    /**
     * 将指定用户加入房间
     * @return
     */
    public boolean joinHome(long uid) {

        //当前用户已经加入此房间则报错
        if (this.hasPlayerInJoinPlayers(uid) == true) {
            throw new NException(NException.SCENE_HOME_USER_ALREADY_JOIN);
        }

        //将当前用户加入房间
        joinPlayersList.add(PlayerManager.getInstance().getPlayer(uid));

        return true;
    }

    /**
     * 指定用户退出房间
     * @return
     */
    public boolean exitHome(long uid) {

        //当前用户不在此房间
        if (this.hasPlayerInJoinPlayers(uid) == false) {
            throw new NException(NException.SCENE_HOME_PLAYER_NOT_EXISTS);
        }

        //移除当前玩家
        Iterator<Player> it = joinPlayersList.iterator();
        while(it.hasNext()){
            Player s = it.next();
            if(s.getUid() == uid){
                s.setHomeId(0);
                it.remove();
            }
        }

        return true;
    }

    /**
     * 消息通道回调方法
     * User: Simon
     * Date: 13-12-25 PM3:16
     */
    class HomeWorkerCallback implements Callback<List<NMessage>> {
        private HomeWorker homeWorker;

        public HomeWorkerCallback(HomeWorker homeWorker) {
            this.homeWorker = homeWorker;
        }

        public void onMessage(List<NMessage> messages) {
            Iterator<NMessage> list = messages.iterator();
            while( list.hasNext() ) {
                NMessage message = list.next();

                homeWorker.channelOnMessage(message);
            }
        }
    }

    /**
     * 指定用户是否在当前房间存在
     * @return
     */
    public boolean hasPlayerInJoinPlayers(long uid) {
        for (Player player : joinPlayersList) {
            if (player.getUid() == uid) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转移当前房间所有权给房间内的其他人
     * @return
     */
    public long transmitHomeToOtherUid() {
        if (joinPlayersList.size() <= 0) {
            throw new NException(NException.SCENE_HOME_PLAYER_EMPTY);
        }

        for (Player p : joinPlayersList) {
            this.ownerUid = p.getUid();
        }

        return this.ownerUid;
    }

    /**
     * 当前房间所有用户是否都已经准备就绪
     * @return
     */
    public boolean isLoadedByAllPlayer() {
        for (Player player : joinPlayersList) {
            if (player.getStatus() != PlayerStatusEnum.LOADED)
                return false;
        }
        return true;
    }

    /**
     * 关闭一个房间
     * @return
     */
    public boolean close() {
        if (status == HomeStatusEnum.DATA_LOADED) {
            fiberThread.dispose();
            return true;
        }

        return false;
    }

    /**
     * 获取房间ID
     * @return
     */
    public int getHomeId() {
        return homeId;
    }

    /**
     * 返回房间关联的地图ID
     * @return
     */
    public int getMapId() {
        return mapId;
    }

    /**
     * 获得加入的玩家数量
     * @return
     */
    public int getJoinPlayersNum() {
        return joinPlayersList.size();
    }

    /**
     * 获取房间状态
     * @return
     */
    public HomeStatusEnum getStatus() {
        return status;
    }

    /**
     * 设置房间状态
     * @param status
     */
    public void setStatus(HomeStatusEnum status) {
        this.status = status;
    }

    /**
     * 获取房间所有者
     * @return
     */
    public long getOwnerUid() {
        return ownerUid;
    }

    /**
     * 返回当前房间加入的用户列表
     * @return
     */
    public List<Player> getJoinPlayersList() {
        return joinPlayersList;
    }

    /**
     * 返回当前房间加入的用户列表
     * @return
     */
    public List<Long> getJoinUidList() {
        List<Long> uidList = new ArrayList<Long>();
        for( Player p : joinPlayersList) {
            uidList.add(p.getUid());
        }
        return uidList;
    }
}
