package com.ninelook.wecard.server.service.common.model.container;

import java.util.HashMap;
import java.util.Map;

/**
 * 指定用户的model列表
 * User: Simon
 * Date: 13-7-31 PM5:37
 */
public class SUserModelList {
    private Map<Class<?>, Map<Long,SModelInfo>> modelsMap = new HashMap<Class<?>, Map<Long,SModelInfo>>();

    public boolean setModelInfo(SModelInfo SModelInfo) {
        Class<?> cls = SModelInfo.getModel().getClass();
        if (modelsMap.get(cls) == null) {
            modelsMap.put(cls, new HashMap<Long,SModelInfo>());
        }

        modelsMap.get(cls).put(SModelInfo.getUnionId(), SModelInfo);
        return true;
    }

    public SModelInfo getModelInfo(Class<?> clazz, long unionId) {
        if (modelsMap.get(clazz) == null) {
            return null;
        }
        return modelsMap.get(clazz).get(unionId);
    }

    public Map<Class<?>, Map<Long,SModelInfo>> getAllModel() {
        return modelsMap;
    }
}
