package com.ninelook.wecard.library.common;

import com.ninelook.wecard.protocol.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送消息接受者
 * User: Simon
 * Date: 13-7-1 下午5:52
 */
public class PushInfoReceiver {
    private static PushInfoReceiver instance;

    private Map<Long, ArrayList<Response.HeshResMessage>> pushInfoMap = new HashMap<Long, ArrayList<Response.HeshResMessage>>();

    public static PushInfoReceiver getInstance() {
        if (instance == null) {
            instance = new PushInfoReceiver();
        }
        return instance;
    }

    public void addPushInfo(long uid, Response.HeshResMessage pushInfo) {
        if (pushInfoMap.get(uid) == null) {
            pushInfoMap.put(uid, new ArrayList<Response.HeshResMessage>());
        }
        pushInfoMap.get(uid).add(pushInfo);
    }

    public List<Response.HeshResMessage> getPushInfoMap(long uid) {
        return pushInfoMap.get(uid);
    }
}
