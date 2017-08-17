package com.ninelook.wecard.server.scene.dispatcher.impl;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.dispatcher.Dispatcher;
import com.ninelook.wecard.server.scene.handler.Handler;
import com.ninelook.wecard.server.scene.handler.HandlerManager;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 场景消息派发器
 * User: Simon
 * Date: 13-12-25 PM4:18
 */
public class SceneDispatcherImpl implements Dispatcher {

    static Logger logger = LogManager.getLogger(SceneDispatcherImpl.class.getName());


    public ExecutorService exec = Executors.newSingleThreadExecutor();

    /**
     * 派发消息
     */
    public void process(final NMessage message) {

        exec.submit(new Runnable() {
            public void run() {
                logger.trace("SceneDispatcherImpl.process ... uid:" + message.getUid()
                                 + ", mid:" + message.getMid());

                Handler h = HandlerManager.getHandler(message.getMid());
                try {
                    h.handle(message);
                } catch (NException e) {
                    NException.catchErr(message.getUid(), "SceneDispatcherImpl.process",
                                                Integer.valueOf(e.getMessage()), e, message.getChannel());

                } catch (Exception e) {
                    NException.catchErr(message.getUid(), "SceneDispatcherImpl.process",
                                                NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
                }
            }
        });

    }

}
