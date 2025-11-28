package com.lubayi.fight.concurrent.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lubayi
 * @date 2025/11/28
 */
public class CompletableFutureDemo {

    public void demo() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 1、使用 runAsync 或 supplyAsync 发起异步调用
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            return "result1";
        }, executor);
        // 2、CompletableFuture.completedFuture() 直接创建一个已完成状态的 CompletableFuture
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("result2");
        // 3、先初始化一个未完成的 CompletableFuture，然后通过 complete()、completeExceptionally()，完成该 CompletableFuture
        // 一个典型使用场景，就是将回调方法转为CompletableFuture，然后再依赖CompletableFuture的能力进行调用编排
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("success");

        // 对于单个CompletableFuture的依赖可以通过 thenApply、thenAccept、thenCompose 等方法来实现
        // 二元依赖可以通过 thenCombine 等回调来实现
        // 多元依赖可以通过 allOf 或 anyOf 方法来实现，区别是当需要多个依赖全部完成时使用allOf，当多个依赖中的任意一个完成即可时使用anyOf
    }

}
