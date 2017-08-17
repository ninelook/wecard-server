package com.ninelook.wecard.server.scene.player;

import com.ninelook.wecard.server.NContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家管理器
 * User: Simon
 * Date: 13-12-17 PM9:15
 */
public class  PlayerManager {
    static Logger logger = LogManager.getLogger(PlayerManager.class.getName());

    private Map<Long, Player> playerMap = new ConcurrentHashMap<Long, Player>();

    public static PlayerManager getInstance() {
        return NContext.getActx().getBean(PlayerManager.class);
    }

    /**
     * 初始化
     */
    public void init() {
        playerMap.clear();
    }

    /**
     * 添加一个玩家
     * @param player
     * @return
     */
    public boolean addPlayer(Player player) {
        if (playerMap.containsKey(player.getUid())) {
            logger.error("PlayerManager.addPlayer ... user already exists.[uid]:" + player.getUid());
            return false;
        }

        playerMap.put(player.getUid(), player);
        return true;
    }

    /**
     * 获取一个玩家
     * @param uid
     * @return
     */
    public Player getPlayer(long uid) {
        if (!playerMap.containsKey(uid)) {
            logger.error("PlayerManager.getPlayer ... user not exists.[uid]:" + uid);
            return null;
        }

        return playerMap.get(uid);
    }

    /**
     * 删除一个玩家
     * @param uid
     * @return
     */
    public boolean removePlayer(Long uid) {
        if (!playerMap.containsKey(uid)) {
            logger.error("PlayerManager.removePlayer ... user not exists.[uid]:" + uid);
            return false;
        }

        playerMap.remove(uid);
        return true;
    }
}
