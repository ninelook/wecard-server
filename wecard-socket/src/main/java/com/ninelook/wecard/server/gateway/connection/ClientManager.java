package com.ninelook.wecard.server.gateway.connection;

import com.google.protobuf.Message;
import com.ninelook.wecard.server.NContext;
import io.netty.channel.Channel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetlang.channels.BatchSubscriber;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.ThreadFiber;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 客户端连接管理器
 *
 * User: Simon
 * Date: 13-12-13 PM2:37
 */
public class ClientManager {
    static Logger logger = LogManager.getLogger(ClientManager.class.getName());


    /**
     * Fiber线程
     */
    private ThreadFiber fiberThread = new ThreadFiber();

    /**
     * 房间消息Channel
     */
    private final org.jetlang.channels.Channel<InvokeFun> invokeChannel = new MemoryChannel<InvokeFun>();

    /**
     * 已连接客户端列表
     */
    private Map<Long, Client> clientMap = new ConcurrentHashMap<Long, Client>();

    public static ClientManager getInstance() {
        return NContext.getActx().getBean(ClientManager.class);
    }

    /**
     * 初始化
     */
    public void init() {
        //回调方法
        Callback<List<InvokeFun>> callback = new ClientManagerCallback(this);

        //像消息通道注册消息处理观察者
        BatchSubscriber<InvokeFun> batchSubscriber = new BatchSubscriber<InvokeFun>(this.fiberThread, callback, 10, TimeUnit.MILLISECONDS);
        invokeChannel.subscribe(this.fiberThread, batchSubscriber);

        this.fiberThread.start();
    }

    /**
     * channel 回调方法
     * @param params
     */
    private void channelOnMessage(InvokeFun params) {
        switch (params.cmdId) {
            case FUN_REGISTER_CLIENT:
                registerClient((Long)params.params.get("uid"), (Channel)params.params.get("channel"));
                break;

            case FUN_UNREGISTER_CLIENT:
                unregisterClient((Long)params.params.get("uid"));
                break;

            case FUN_BROADCAST_MESSAGE:
                broadcast((Message)params.params.get("message"));
                break;

            case FUN_SEND_MESSAGE:
                send((Long)params.params.get("uid"), (Message)params.params.get("message"));
                break;

            case FUN_SOME_SEND_MESSAGE:
                sendSome((List<Long>)params.params.get("uids"), (Message)params.params.get("message"));
                break;
        }
    }

    /**
     * 销毁一个客户端的连接
     */
    public void facadeUnregisterClient(long uid) {
        logger.trace("ClientManager.facadeUnregisterClient ... uid:" + uid);

        InvokeFun fun = new InvokeFun();
        fun.cmdId = InvokeFunCmdIdEnums.FUN_UNREGISTER_CLIENT;
        fun.params.put("uid", uid);

        invokeChannel.publish(fun);
    }

    /**
     * 注册一个客户端的连接
     * @param uid
     * @param channel
     */
    public void facadeRegisterClient(long uid, Channel channel) {
        logger.trace("ClientManager.facadeRegisterClient ... uid:" + uid);

        InvokeFun fun = new InvokeFun();
        fun.cmdId = InvokeFunCmdIdEnums.FUN_REGISTER_CLIENT;
        fun.params.put("uid", uid);
        fun.params.put("channel", channel);

        invokeChannel.publish(fun);
    }

    /**
     * 广播信息
     * @param message
     */
    public void facadeBroadcast(Message message) {
        logger.trace("ClientManager.facadeBroadcast");

        InvokeFun fun = new InvokeFun();
        fun.cmdId = InvokeFunCmdIdEnums.FUN_BROADCAST_MESSAGE;
        fun.params.put("message", message);

        invokeChannel.publish(fun);
    }

    /**
     * 发送信息
     * @param message
     */
    public void facadeSend(long uid, Message message) {
        logger.trace("ClientManager.facadeSend ... uid:" + uid);

        InvokeFun fun = new InvokeFun();
        fun.cmdId = InvokeFunCmdIdEnums.FUN_SEND_MESSAGE;
        fun.params.put("message", message);
        fun.params.put("uid", uid);

        invokeChannel.publish(fun);
    }

    /**
     * 发送信息
     * @param message
     */
    public void facadeSendSome(List<Long> uids, Message message) {
        logger.trace("ClientManager.facadeSendSome ... uids:" + uids);

        InvokeFun fun = new InvokeFun();
        fun.cmdId = InvokeFunCmdIdEnums.FUN_SOME_SEND_MESSAGE;
        fun.params.put("message", message);
        fun.params.put("uids", uids);

        invokeChannel.publish(fun);
    }

    /**
     * 注册一个客户端的连接
     * @param uid
     * @param channel
     */
    private void registerClient(long uid, Channel channel) {
        logger.trace("ClientManager.registerClient ... uid:" + uid);

        Client client = new Client(uid, channel);
        clientMap.put(uid, client);
    }

    /**
     * 销毁一个客户端的连接
     */
    private void unregisterClient(long uid) {
        logger.trace("ClientManager.unregisterClient ... uid:" + uid);

        Client client = clientMap.get(uid);
        if (client != null) {
            client.disconnect();
            clientMap.remove(uid);
        }
    }

    /**
     * 广播一个消息
     * @param message
     */
    private void broadcast(Message message) {
        logger.trace("ClientManager.broadcast");
        for( Map.Entry<Long, Client> en : clientMap.entrySet()) {
            en.getValue().send(message);
        }
    }

    /**
     * 发送消息给指定人
     * @param message
     */
    private void send(long uid, Message message) {
        logger.trace("ClientManager.send ... uid:" + uid);
        if (clientMap.containsKey(uid)) {
            clientMap.get(uid).send(message);
        }
    }

    /**
     * 发送信息给一批人
     * @param message
     */
    private void sendSome(List<Long> uids, Message message) {
        logger.trace("ClientManager.sendSome ... uid:" + uids);
        for (long uid : uids) {
            send(uid, message);
        }
    }

    /**
     * 消息通道回调方法
     * User: Simon
     * Date: 13-12-25 PM3:16
     */
    private class ClientManagerCallback implements Callback<List<InvokeFun>> {
        private ClientManager clientManager;

        public ClientManagerCallback(ClientManager clientManager) {
            this.clientManager = clientManager;
        }

        public void onMessage(List<InvokeFun> cmds) {
            Iterator<InvokeFun> list = cmds.iterator();
            //TODO:此部分可以采用消息合并机制, 分次发送的信息并成一次发送给客户端.
            while( list.hasNext() ) {
                InvokeFun cmd = list.next();

                clientManager.channelOnMessage(cmd);
            }
        }
    }

    /**
     * actor模式的调用方法
     */
    private class InvokeFun {
        InvokeFunCmdIdEnums cmdId;
        Map params = new HashMap();
    }


    /**
     * 方法集枚举
     */
    private enum InvokeFunCmdIdEnums {
        //注册一个Client
        FUN_REGISTER_CLIENT,

        //销毁一个client
        FUN_UNREGISTER_CLIENT,

        //广播信息
        FUN_BROADCAST_MESSAGE,

        //单点发送信息
        FUN_SEND_MESSAGE,

        //单点发送信息
        FUN_SOME_SEND_MESSAGE,
    }
}
