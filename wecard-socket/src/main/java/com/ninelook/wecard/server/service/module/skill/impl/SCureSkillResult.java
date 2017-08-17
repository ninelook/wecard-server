package com.ninelook.wecard.server.service.module.skill.impl;

import com.ninelook.wecard.server.service.module.skill.SSkillResult;

import java.util.Map;

/**
 * 加血技能返回结果
 * User: Simon
 * Date: 3/5/14 15:47
 */
public class SCureSkillResult implements SSkillResult {
    private Map<Integer, Integer> addBloodMap;

    public Map<Integer, Integer> getAddBloodMap() {
        return addBloodMap;
    }

    public void setAddBloodMap(Map<Integer, Integer> addBloodMap) {
        this.addBloodMap = addBloodMap;
    }
}
