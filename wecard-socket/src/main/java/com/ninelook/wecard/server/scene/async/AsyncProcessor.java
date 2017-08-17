package com.ninelook.wecard.server.scene.async;

import com.ninelook.wecard.server.NContext;
import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.scene.message.NMessage;
import org.jetlang.channels.BatchSubscriber;
import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 异步IO处理类
 * User: Simon
 * Date: 2/27/14 11:02
 */
public class AsyncProcessor {
    /**
     * 房间消息Channel
     */
    private final Channel<NMessage> messageChannel = new MemoryChannel<NMessage>();

    /**
     * fiber线程
     */
    private Fiber fiberThread;

    public static AsyncProcessor getInstance() {
        return NContext.getActx().getBean(AsyncProcessor.class);
    }

    public AsyncProcessor() {
        ExecutorService exec = Executors.newCachedThreadPool();
        PoolFiberFactory factory = new PoolFiberFactory(exec);

        this.fiberThread = factory.create();

        //回调方法
        Callback<List<NMessage>> callback = new AsyncCallback(this);

        //像消息通道注册消息处理观察者
        BatchSubscriber<NMessage> batchSubscriber = new BatchSubscriber<NMessage>(this.fiberThread, callback, 30, TimeUnit.MILLISECONDS);
        this.messageChannel.subscribe(this.fiberThread, batchSubscriber);
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
            if (message instanceof MessageHaveHandler) { //消息具有Handler关联, 一般为内部接口
                ((MessageHaveHandler) message).exeHandler();
            } else {
                throw new NException(NException.ASYNC_NOT_HAVE_HANDLER);
            }

        } catch (NException e) {
            NException.catchErr(message.getUid(), "AsyncProcessor.channelOnMessage",
                                        Integer.valueOf(e.getMessage()), e, message.getChannel());
        } catch (RuntimeException e) {
            e.printStackTrace();

        } catch (Exception e) {
            NException.catchErr(message.getUid(), "AsyncProcessor.channelOnMessage",
                                        NException.ERROR_UNCATCH_EXCEPTION, e, message.getChannel());
        }
    }

    /**
     * 消息通道回调方法
     */
    class AsyncCallback implements Callback<List<NMessage>> {
        private AsyncProcessor asyncProcessor;

        public AsyncCallback(AsyncProcessor asyncProcessor) {
            this.asyncProcessor = asyncProcessor;
        }

        public void onMessage(List<NMessage> messages) {
            Iterator<NMessage> list = messages.iterator();
            while( list.hasNext() ) {
                NMessage message = list.next();

                asyncProcessor.channelOnMessage(message);
            }
        }
    }
}