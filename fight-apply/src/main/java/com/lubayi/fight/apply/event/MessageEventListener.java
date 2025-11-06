package com.lubayi.fight.apply.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Author: lubayi
 * Date: 2025/11/6
 * Time: 21:17
 */
@Component
public class MessageEventListener {

    @EventListener
    @Async
    public void sendMessage(MessageEvent event) {
        ProjectInfo projectInfo = (ProjectInfo) event.getSource();
        for (int i = 0; i < 100; i++) {
            System.out.println(projectInfo.getName() + "已经开始，请" + i + "用户参加！");
        }
    }

}
