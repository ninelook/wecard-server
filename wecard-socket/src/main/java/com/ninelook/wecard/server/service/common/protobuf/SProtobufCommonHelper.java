package com.ninelook.wecard.server.service.common.protobuf;

import com.ninelook.wecard.protocol.beans.BeanCommonMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * protobuf战斗助手类
 * User: Simon
 * Date: 13-12-29 PM4:18
 */
public class SProtobufCommonHelper {

    /**
     * 返回Item列表 - Integer:Integer
     * @return
     */
    public static List<BeanCommonMessage.ItemByIntInt> getItemIntegerIntegerList(Map<Integer, Integer> maps) {
        List<BeanCommonMessage.ItemByIntInt> list = new ArrayList<BeanCommonMessage.ItemByIntInt>();
        for (Map.Entry<Integer, Integer> en : maps.entrySet()) {
            BeanCommonMessage.ItemByIntInt.Builder item = BeanCommonMessage.ItemByIntInt.newBuilder();
            item.setKey(en.getKey());
            item.setValue(en.getValue());

            list.add(item.build());
        }

        return list;
    }

}
