package com.ninelook.wecard.server.service.module.build;

import com.ninelook.wecard.server.service.module.ai.SBuildAI;
import com.ninelook.wecard.server.service.module.entity.SEntityTypeEnum;
import com.ninelook.wecard.server.service.module.entity.SUnit;

/**
 * 建筑entity
 * User: Simon
 * Date: 14-1-4 上午11:19
 */
public class SBuildEntity extends SUnit {

    public SBuildEntity() {
        super(SEntityTypeEnum.TYPE_BUILD);
    }

    @Override
    public boolean initAI() {
        this.aiEngine = new SBuildAI(this);
        return true;
    }

}
