package com.ninelook.wecard.server.scene.dispatcher;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.dispatcher.impl.ServiceDispatcherImpl;
import com.ninelook.wecard.server.scene.message.NMessage;
import com.ninelook.wecard.server.scene.message.NMessageId;
import com.ninelook.wecard.server.NMidConstant;
import com.ninelook.wecard.server.scene.dispatcher.impl.SceneDispatcherImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 中心消息派发器(所有消息的入口)
 * User: Simon
 * Date: 13-12-15 PM11:10
 */
public class MainRoute {

    static Logger logger = LogManager.getLogger(MainRoute.class.getName());

    //线程数量
    public static final int THREAD_NUM = 1;

    //线程池
    private ExecutorService exec = Executors.newFixedThreadPool(THREAD_NUM);

    /**
     * 房间内部消息派发器
     */
    protected ServiceDispatcherImpl serviceDispatcher;

    /**
     * 场景派发器
     */
    protected SceneDispatcherImpl sceneDispatcher;


    public void route(final NMessage message) {
        logger.info("MainDispatcher.route ... uid:" + message.getUid()
                         + ", mid:" + message.getMid());

        //验证用户ID是否合法, 小于等于1均为非法(1为内部公共用户)
        if (message.getUid() <= 1) {
            throw new NException(NException.ERROR_UID_VALID_FAIL);
        }

        //解析消息
        try {
            NMessageId messageId = NMessageId.parse(message.getMid());

            //路由消息
            switch (messageId.getModId()) {

                //房间内部消息
                case NMidConstant.MESSAGE_MOD_SERVICE:
                    getServiceDispatcher().process(message);
                    break;

                //场景消息
                case NMidConstant.MESSAGE_MOD_SCENE:
                    getSceneDispatcher().process(message);
                    break;

                default:
                    break;
            }

        } catch (NException e) {
            NException.catchErr(message.getUid(), "MainDispatcher.route",
                                        Integer.valueOf(e.getMessage()), e, message.getChannel());

        } catch (Exception e) {
            NException.catchErr(message.getUid(), "MainDispatcher.route",
                                        NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
        }

    }

    /**
     * 家园消息派发器
     * @return
     */
    protected Dispatcher getServiceDispatcher() {
        if (serviceDispatcher == null) {
            serviceDispatcher = new ServiceDispatcherImpl();
        }

        return serviceDispatcher;
    }

    /**
     * 场景消息派发器
     * @return
     */
    protected Dispatcher getSceneDispatcher() {
        if (sceneDispatcher == null) {
            sceneDispatcher = new SceneDispatcherImpl();
        }
        return sceneDispatcher;
    }
}
