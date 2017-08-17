package com.ninelook.wecard.server.service.common.model.container;

import com.ninelook.wecard.server.service.common.model.SAbstractModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块容器
 * User: Simon
 * Date: 13-7-29 PM4:34
 */
public class SContainerManager {
    private static HashMap<Integer, SContainerManager> containersMap = new HashMap<Integer, SContainerManager>();

    private static final ThreadLocal<Integer> ctxHomeId = new ThreadLocal<Integer>();

    private Map<Long, SUserModelList> containerMap = new HashMap<Long, SUserModelList>();

    private Map<Long, SUserModelList> getContainerMap() {
        return containerMap;
    }

    public static SContainerManager getInstance() {
        SContainerManager container = containersMap.get(getCtxHomeId());

        if (container == null) {
            container = new SContainerManager();
            containersMap.put(getCtxHomeId(), container);
        }

        return container;
    }

    /**
     * 设置模块
     * @param uid
     * @param model
     * @return
     */
    public boolean set(long uid, SAbstractModel model) {
        SUserModelList SUserModelList;

        if (!getContainerMap().containsKey(uid)) {
            SUserModelList = new SUserModelList();
            getContainerMap().put(uid, SUserModelList);
        }

        SUserModelList = getContainerMap().get(uid);

        SModelInfo SModelInfo = new SModelInfo();
        SModelInfo.setUid(uid);
        SModelInfo.setUnionId(model.getUnionId());
        SModelInfo.setModel(model);

        SUserModelList.setModelInfo(SModelInfo);

        return true;
    }

    /**
     * 获取模块
     * @param uid
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(long uid, Class<?> clazz) {
        return innerGet(uid, 0, clazz);
    }

    /**
     * 获取模块
     * @param uid
     * @param unionId
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(long uid, long unionId, Class<?> clazz) {
        return innerGet(uid, unionId, clazz);
    }

    /**
     * 获取模块
     * @param uid
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T innerGet(long uid, long unionId, Class<?> clazz) {
        if (!getContainerMap().containsKey(uid)) {
            return null;
        }

        SUserModelList SUserModelList = getContainerMap().get(uid);
        SModelInfo SModelInfo;
        SModelInfo = SUserModelList.getModelInfo(clazz, unionId);

        if (SModelInfo == null) {
            return null;
        }

        return (T) SModelInfo.getModel();
    }

    /**
     * 清除所有model
     */
    public void deleteAll() {
        getContainerMap().clear();
    }

    /**
     * 清除指定用户的所有model
     * @param uid
     */
    public void delete(long uid) {
        getContainerMap().remove(uid);
    }

    /**
     * 所有模块统一保存
     */
    public void save() {
        for(Map.Entry<Long, SUserModelList> item : getContainerMap().entrySet()) {
            Map<Class<?>, Map<Long,SModelInfo>> modelList = item.getValue().getAllModel();
            if (modelList != null) {
                for(Map.Entry<Class<?>, Map<Long,SModelInfo>> models : modelList.entrySet()) {
                    for (Map.Entry<Long, SModelInfo> modelEntry : models.getValue().entrySet()) {
                        modelEntry.getValue().getModel().save();
                    }
                }
            }
        }
    }

    /**
     * 当前线程上下文数据, 容器也随即会被清除
     */
    public static void clear() {
        containersMap.remove(getCtxHomeId());
    }

    /**
     * 设置当前线程处理的homeId
     * @param homeId
     */
    public static void setCtxHomeId(int homeId) {
        ctxHomeId.set(homeId);
    }

    /**
     * 返回当前线程处理的homeId
     * @return
     */
    public static int getCtxHomeId() {
        return ctxHomeId.get();
    }
}
