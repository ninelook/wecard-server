package com.ninelook.wecard.library.common;

import java.util.Arrays;
import java.util.List;

/**
 * 通用ID管理类(接口测试/单元测试使用)
 * User: Simon
 * Date: 13-7-1 下午5:52
 */
public class IdManager {
    private static IdManager instance;

    public static IdManager getInstance() {
        if (instance == null) {
            instance = new IdManager();
        }
        return instance;
    }

    /**
     * 用户ID
     */
    public List<Long> uids = Arrays.asList(
                                              9001L,
                                              9002L,
                                              9003L,
                                              9004L
    );

    /**
     * 用户ID
     */
    public List<Integer> mapIds = Arrays.asList(
                                               19305
    );


}
