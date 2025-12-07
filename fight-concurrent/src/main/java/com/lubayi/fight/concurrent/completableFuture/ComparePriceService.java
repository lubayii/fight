package com.lubayi.fight.concurrent.completableFuture;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lubayi
 * @date 2025/11/28
 */
public class ComparePriceService {

    private ExecutorService threadPool = Executors.newFixedThreadPool(5);

    /**
     * 【串行】获取多个平台比价信息得到最低价格平台
     * @param product
     * @return
     */
    public PriceResult getCheapestPlatAndPrice(String product) {
        PriceResult mouBaoPrice = computeRealPrice(HttpRequestMock.getMouBaoPrice(product),
                HttpRequestMock.getMouBaoDiscounts(product));
        PriceResult mouDongPrice = computeRealPrice(HttpRequestMock.getMouDongPrice(product),
                HttpRequestMock.getMouDongDiscounts(product));
        PriceResult mouXixiPrice = computeRealPrice(HttpRequestMock.getMouXixiPrice(product),
                HttpRequestMock.getMouXixiDiscounts(product));

        return Stream.of(mouBaoPrice, mouDongPrice, mouXixiPrice)
                .min(Comparator.comparingInt(PriceResult::getRealPrice))
                .get();
    }

    /**
     * 演示传统方式通过线程池来增加并发
     * @param product
     * @return
     */
    public PriceResult getCheapestPlatAndPrice2(String product) {
        Future<PriceResult> mouBaoFuture = threadPool.submit(() ->
                computeRealPrice(HttpRequestMock.getMouBaoPrice(product), HttpRequestMock.getMouBaoDiscounts(product)));
        Future<PriceResult> mouDongFuture = threadPool.submit(() ->
                computeRealPrice(HttpRequestMock.getMouDongPrice(product), HttpRequestMock.getMouDongDiscounts(product)));
        Future<PriceResult> mouXixiFuture = threadPool.submit(() ->
                computeRealPrice(HttpRequestMock.getMouXixiPrice(product), HttpRequestMock.getMouXixiDiscounts(product)));

        // 等待所有线程结果都处理完成，然后从结果中计算出最低价
        return Stream.of(mouBaoFuture, mouDongFuture, mouXixiFuture)
                .map(priceResultFuture -> {
                    try {
                        return priceResultFuture.get(5L, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparingInt(PriceResult::getRealPrice))
                .get();
    }

    /**
     * 演示并行处理的模式
     * @param product
     * @return
     */
    public PriceResult getCheapestPlatAndPrice3(String product) {
        CompletableFuture<PriceResult> mouBao = CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoPrice(product))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoDiscounts(product)),
                        this::computeRealPrice);
        CompletableFuture<PriceResult> mouDong = CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouDongPrice(product))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouDongDiscounts(product)),
                        this::computeRealPrice);
        CompletableFuture<PriceResult> mouXixi = CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiPrice(product))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiDiscounts(product)),
                        this::computeRealPrice);

        return Stream.of(mouBao, mouDong, mouXixi)
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(PriceResult::getRealPrice))
                .findFirst()
                .get();
    }

    public PriceResult getCheapestPlatAndPrice4(String product) {
        // 构造自定义线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);

        return CompletableFuture.supplyAsync(
                () -> HttpRequestMock.getMouXixiPrice(product), executor
        ).thenCombineAsync(
                CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiDiscounts(product)),
                this::computeRealPrice,
                executor
        ).join();
    }

    public PriceResult getCheapestPlatAndPrice5(String product) {
        return CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiPrice(product))
                .thenCombine(
                        CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiDiscounts(product)),
                        this::computeRealPrice
                ).join();
    }

    public PriceResult comparePriceInOnePlat(List<String> products) {
        return products.stream()
                .map(product ->
                        CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoPrice(product))
                                .thenCombine(
                                        CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoDiscounts(product)),
                                        this::computeRealPrice
                                ))
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(PriceResult::getRealPrice))
                .findFirst()
                .get();
    }

    public PriceResult comparePriceInOnePlat2(List<String> products) {
        List<CompletableFuture<PriceResult>> completableFutures = products.stream()
                .map(product ->
                        CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoPrice(product))
                                .thenCombine(
                                        CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoDiscounts(product)),
                                        this::computeRealPrice
                                ))
                .collect(Collectors.toList());

        return completableFutures.stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(PriceResult::getRealPrice))
                .findFirst()
                .get();
    }


    private PriceResult computeRealPrice(PriceResult priceResult, int disCounts) {
        priceResult.setRealPrice(priceResult.getPrice() - disCounts);
        LogHelper.printLog(priceResult.getPlatform() + "最终价格计算完成：" + priceResult.getRealPrice());
        return priceResult;
    }

    public void testCreateFuture(String product) {
        // supplyAsync 执行逻辑有返回值
        CompletableFuture<PriceResult> supplyAsyncResult =
                CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouBaoPrice(product));
        // runAsync 执行逻辑没有返回值
        CompletableFuture<Void> runAsyncResult =
                CompletableFuture.runAsync(() -> System.out.println(product));
        // supplyAsync、runAsync 创建后便会立即执行，不需要手动调用触发
    }

    public void testGetAndJoin(String product) {
        // join 无需显式 try...catch...
        PriceResult joinResult = CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiPrice(product))
                .join();

        try {
            // get 显式try...catch
            PriceResult getResult = CompletableFuture.supplyAsync(() -> HttpRequestMock.getMouXixiPrice(product))
                    .get(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void testExceptionHandle() {
        // supplyAsync 抛出异常后，后面的 thenApply 并没有被执行。
//        CompletableFuture.supplyAsync(() -> {
//            throw new RuntimeException("supplyAsync exception occurred...");
//        }).thenApply(obj -> {
//            System.out.println("thenApply executed...");
//            return obj;
//        }).join();


        // handle，与thenApply类似，区别点在于handle执行函数的入参有两个，
        // 一个是CompletableFuture执行的实际结果，一个是是Throwable对象，这样如果前面执行出现异常的时候，可以通过handle获取到异常并进行处理。
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("supplyAsync exception occurred...");
        }).handle((obj, e) -> {
            if (e != null) {
                System.out.println("thenApply executed，" + e.getMessage());
            }
            return obj;
        }).join();  // 输出：thenApply executed，java.lang.RuntimeException: supplyAsync exception occurred...

    }


    public static void main(String[] args) {
        ComparePriceService service = new ComparePriceService();
        long startTime = System.currentTimeMillis();
//        PriceResult result = service.getCheapestPlatAndPrice5("Iphone13");
//        System.out.println("获取最优价格信息：" + result);

        PriceResult result = service.comparePriceInOnePlat2(Arrays.asList("Iphone13黑色", "Iphone13白色", "Iphone红色"));
        System.out.println("获取最优价格信息：" + result);

        System.out.println("-----执行耗时： " + (System.currentTimeMillis() - startTime) + "ms  ------");
    }

}
