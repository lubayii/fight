package com.lubayi.fight.apply.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: lubayi
 * Date: 2025/11/6
 * Time: 21:17
 */
@Component
public class MessageEventListener {

    @EventListener
    @Async("smsExecutor")
    public void sendMessage(MessageEvent event) {
        ProjectInfo projectInfo = (ProjectInfo) event.getSource();
        long startTime = System.currentTimeMillis();
        List<Integer> ids = getIds();
        ids.forEach(id -> send(projectInfo, id));
        long endTime = System.currentTimeMillis();
        System.out.println("--------------总耗时：" + (endTime - startTime) + " ms");
    }

    private void send(ProjectInfo projectInfo, Integer id) {
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "===========" +
                projectInfo.getName() + "已经开始，请" + id + "用户参加！");
    }

    private List<Integer> getIds() {
        List<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        return list;
    }

}
