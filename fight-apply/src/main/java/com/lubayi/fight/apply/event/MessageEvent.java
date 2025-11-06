package com.lubayi.fight.apply.event;

import org.springframework.context.ApplicationEvent;

/**
 * Author: lubayi
 * Date: 2025/11/6
 * Time: 21:08
 */
public class MessageEvent extends ApplicationEvent {

    public MessageEvent(ProjectInfo projectInfo) {
        super(projectInfo);
    }

}
