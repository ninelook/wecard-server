package com.ninelook.wecard.server.scene.home;

import com.ninelook.wecard.server.NContext;
import com.ninelook.wecard.server.NException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetlang.fibers.ThreadFiber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 房间管理器
 * User: Simon
 * Date: 13-12-17 PM9:14
 */
public class HomeManager {
    static Logger logger = LogManager.getLogger(HomeManager.class.getName());

    //房间工作线程数量
    private static final int THREAD_NUM = 10;

    /**
     * Fiber的Exec.
     */
    private List<ThreadFiber> threadFibers = new ArrayList<ThreadFiber>();

    /**
     * 房间列表
     */
    protected Map<Integer, HomeWorker> homeWorkerMap = new ConcurrentHashMap<Integer, HomeWorker>();

    /**
     * 各地图下拥有的房间ID列表
     */
    protected Map<Integer, ArrayList<Integer>> homeIdListByMapId = new HashMap<Integer, ArrayList<Integer>>();

    /**
     * 房间ID生成器
     */
    private AtomicInteger homeIdGenerator = new AtomicInteger(0);


    public static HomeManager getInstance() {
        return NContext.getActx().getBean(HomeManager.class);
    }

    /**
     * 初始化
     */
    public void init() {
        //fiber exec init
        for(int i = 0; i < THREAD_NUM; i++) {
            ThreadFiber threadFiber = new ThreadFiber();

            //设置线程异常捕获
            threadFiber.getThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    logger.error("HomeManager ... uncaughtException" + " ... " + e.toString());
                    e.printStackTrace();
                }
            });

            threadFiber.start();
            this.threadFibers.add(i, threadFiber);
        }
    }

    /**
     * 建立一个房间
     * @return
     */
    public HomeWorker createHomeWorker(long ownerId, int mapId) {
        int homeId = getNewHomeId();

        ThreadFiber threadFiber = getThreadFiber(homeId);

        HomeWorker homeWorker = new HomeWorker(threadFiber, mapId, homeId, ownerId);

        homeWorkerMap.put(homeId, homeWorker);

        ArrayList<Integer> homeIdList = homeIdListByMapId.get(mapId);
        if (homeIdList == null) {
            homeIdList = new ArrayList<Integer>();
            homeIdListByMapId.put(mapId, homeIdList);
        }

        //当前房间内存在此房间homeId则报错
        if (homeIdList.contains(homeId)) {
            throw new NException(NException.SCENE_HOME_MAP_HOMEID_EXISTS);
        }
        homeIdList.add(homeId);

        return homeWorker;
    }

    private ThreadFiber getThreadFiber(int homeId) {
        ThreadFiber t = this.threadFibers.get(homeId % THREAD_NUM);

        if (t.getThread().isAlive() == false) {
            logger.error("HomeManager.getThreadFiber ... THREAD NOT ALIVE!!!  homeId:" + homeId);
        }

        return t;
    }

    /**
     * 获取一个房间
     * @return
     */
    public HomeWorker getHomeWorker(int homeId) {

        //验证当前房间是否存在
        if (!homeWorkerMap.containsKey(homeId)) {
            throw new NException(NException.SCENE_HOME_NOT_EXISTS);
        }

        return homeWorkerMap.get(homeId);
    }

    /**
     * 获取指定mapId下的房间列表
     * @param mapId
     * @return
     */
    public List<HomeWorker> getHomeWorkerListByMapId(int mapId) {
        List<HomeWorker> homeWorkerList = new ArrayList<HomeWorker>();

        List<Integer> homeIdList = homeIdListByMapId.get(mapId);

        if (homeIdList != null) {
            for (int homeId : homeIdList) {
                homeWorkerList.add(getHomeWorker(homeId));
            }
        }

        return homeWorkerList;
    }

    /**
     * 关闭一个房间
     * @param homeId
     * @return
     */
    public boolean closeHome(int homeId) {
        //关闭当前房间
        HomeWorker homeWorker = homeWorkerMap.get(homeId);

        //房间内必须没有玩家存在才可以关闭
        if (homeWorker.getJoinPlayersNum() > 0) {
            throw new NException(NException.SCENE_HOME_PLAYER_EXISTS);
        }

        //关闭房间线程
        homeWorker.close();

        //删除当前房间
        homeWorkerMap.remove(homeId);

        int mapId = homeWorker.getMapId();

        //mapId分组内移除
        List<Integer> homeIdList = homeIdListByMapId.get(mapId);
        if (homeIdList != null && homeIdList.size() > 0) {
            for(int i = 0; i < homeIdList.size(); i++)
            {
                if(homeIdList.get(i).equals(homeId)){
                    homeIdList.remove(i);

                    //如果为空, 则清楚当前项
                    if (homeIdList.size() <= 0) {
                        homeIdListByMapId.remove(mapId);
                    }
                }
            }
        }


        return true;
    }

    /**
     * 退出一个房间
     * @param homeId
     * @return
     */
    public int exitHome(int homeId, long uid) {
        //关闭当前房间
        HomeWorker homeWorker = homeWorkerMap.get(homeId);
        homeWorker.exitHome(uid);

        //获取当前房间玩家数量
        int playerNum = homeWorker.getJoinPlayersNum();

        return playerNum;
    }

    public Map<Integer, HomeWorker> getHomeWorkerMap() {
        return homeWorkerMap;
    }

    /**
     * 返回一个新的房间ID
     * @return
     */
    private int getNewHomeId() {
        return homeIdGenerator.incrementAndGet();
    }

    public AtomicInteger getHomeIdGenerator() {
        return homeIdGenerator;
    }
}
