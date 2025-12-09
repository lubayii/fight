package com.lubayi.fight.apply.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Author: lubayi
 * Date: 2025/11/6
 * Time: 21:17
 */
@Component
public class MessageEventListener {

    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @EventListener
    @Async("smsExecutor")
    public void sendMessage(MessageEvent event) {
        ProjectInfo projectInfo = (ProjectInfo) event.getSource();
        long startTime = System.currentTimeMillis();
        List<Integer> ids = getIds();
        List<CompletableFuture<Void>> collect = ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> send(projectInfo, id), threadPool))
                .collect(Collectors.toList());  // 总耗时：3042ms
        CompletableFuture.allOf(collect.toArray(new CompletableFuture[0])).join();
        // ids.forEach(id -> send(projectInfo, id));    // 总耗时：30416ms
        long endTime = System.currentTimeMillis();
        System.out.println("--------------总耗时：" + (endTime - startTime) + " ms");
    }

    public Void send(ProjectInfo projectInfo, Integer id) {
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "===========" +
                projectInfo.getName() + "已经开始，请" + id + "用户参加！");
        return null;
    }

    private List<Integer> getIds() {
        List<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        return list;
    }

}
