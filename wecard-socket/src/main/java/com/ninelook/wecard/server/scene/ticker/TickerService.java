package com.ninelook.wecard.server.scene.ticker;

import com.ninelook.wecard.server.NContext;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.home.HomeStatusEnum;
import com.ninelook.wecard.server.scene.home.HomeWorker;
import com.ninelook.wecard.server.service.handler.ticker.inner.message.STickerNoticeInnerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 滴答服务
 *
 * 按照指定间隔向各房间发送滴答
 * User: Simon
 * Date: 2/19/14 19:22
 */
public class TickerService {
    static Logger logger = LogManager.getLogger(TickerService.class.getName());

    private Thread tickerThread;

    public static TickerService getInstance() {
        return NContext.getActx().getBean(TickerService.class);
    }

    /**
     * 启动滴答服务
     */
    public void start() {
        this.tickerThread = new Thread(new TickerRunnable());
        this.tickerThread.start();
    }

    /**
     * 滴答循环方法
     */
    class TickerRunnable implements Runnable  {
        public void run() {
            HomeManager homeManager = HomeManager.getInstance();

            HomeWorker homeWorker;

            while (true) {
                try {
                    for (Map.Entry<Integer, HomeWorker> en : homeManager.getHomeWorkerMap().entrySet()) {
                        homeWorker = en.getValue();

                        //战斗中的房间才发送通知
                        if (homeWorker.getStatus() != HomeStatusEnum.FIGHT_DOING) {
                            continue;
                        }

                        //向房间服务发送滴答消息
                        STickerNoticeInnerMessage tickerNoticeInnerMessage = new STickerNoticeInnerMessage(0);
                        tickerNoticeInnerMessage.setHomeId(en.getValue().getHomeId());
                        en.getValue().sendMessage(tickerNoticeInnerMessage);
                    }
                } catch (Exception e) {
                    logger.error("TickerService.TickerRunnable ... error:" + e.getMessage());
                    e.printStackTrace();
                }

                //一秒执行10次
                try { Thread.sleep(300); } catch (Exception e) {}
            }
        }
    }


}
