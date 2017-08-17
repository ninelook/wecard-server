package com.ninelook.wecard.library.common;

import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.protocol.Request;

import java.util.Map;

/**
 * User: Simon
 * Date: 13-7-2 下午12:14
 */
public class TestUtil {
    public static boolean compareMap(Map<Object, Object> a, Map<Object, Object> b) {
        for( Map.Entry<Object, Object> e : a.entrySet() ) {
            Object akey = e.getKey();
            Object bValue = b.get(akey);
            Object aValue = e.getValue();
            if(bValue == null) return false;

            if(!aValue.equals(bValue)) return false;
        }
        return true;
    }

    public static Communication.HeshReqMessage.Builder getReqSceneMessage() {
        Request.SceneReqMessage.Builder sceneReqMessageBuilder = Request.SceneReqMessage.newBuilder();

        Communication.HeshReqMessage.Builder messageBuilder = Communication.HeshReqMessage.newBuilder();
        messageBuilder.setSceneReqMessage(sceneReqMessageBuilder);

        return messageBuilder;
    }
}
