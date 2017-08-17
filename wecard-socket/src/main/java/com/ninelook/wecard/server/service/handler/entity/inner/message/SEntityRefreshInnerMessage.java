package com.ninelook.wecard.server.service.handler.entity.inner.message;

import com.ninelook.wecard.server.scene.message.MessageHaveHandler;
import com.ninelook.wecard.server.service.common.message.SServiceInnerMessage;
import com.ninelook.wecard.server.service.handler.entity.inner.handler.SEntityRefreshInnerHandler;
import com.ninelook.wecard.server.service.module.entity.SUnit;

import java.util.List;

/**
 *  实体刷新推送
 */
public class SEntityRefreshInnerMessage extends SServiceInnerMessage implements MessageHaveHandler {
    private List<SUnit> sEntityList;

    public SEntityRefreshInnerMessage() {
        super(0);
    }

    public List<SUnit> getsEntityList() {
        return sEntityList;
    }

    public void setEntityList(List<SUnit> sEntityList) {
        this.sEntityList = sEntityList;
    }

    @Override
    public void exeHandler() {
        new SEntityRefreshInnerHandler().handle(this);
    }
}
