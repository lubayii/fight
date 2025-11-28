package com.lubayi.fight.concurrent.completableFuture;

/**
 * @author lubayi
 * @date 2025/11/28
 */
// 模拟对外请求的具体耗时服务
public class HttpRequestMock {

    public static PriceResult getMouBaoPrice(String product) {
        LogHelper.printLog("获取某宝上" + product + "的价格");;
        mockCostTimeOperation();

        PriceResult result = new PriceResult("某宝");
        result.setPrice(5199);
        LogHelper.printLog("获取某宝上" + product + "的价格完成：5199");
        return result;
    }

    public static int getMouBaoDiscounts(String product) {
        LogHelper.printLog("获取某宝上" + product + "的优惠");;
        mockCostTimeOperation();
        LogHelper.printLog("获取某宝上" + product + "的优惠完成：-200");
        return 200;
    }

    public static PriceResult getMouDongPrice(String product) {
        LogHelper.printLog("获取某东上" + product + "的价格");;
        mockCostTimeOperation();

        PriceResult result = new PriceResult("某东");
        result.setPrice(5299);
        LogHelper.printLog("获取某东上" + product + "的价格完成：5299");
        return result;
    }

    public static int getMouDongDiscounts(String product) {
        LogHelper.printLog("获取某东上" + product + "的优惠");;
        mockCostTimeOperation();
        LogHelper.printLog("获取某东上" + product + "的优惠完成：-150");
        return 150;
    }

    public static PriceResult getMouXixiPrice(String product) {
        LogHelper.printLog("获取某夕夕上" + product + "的价格");;
        mockCostTimeOperation();

        PriceResult result = new PriceResult("某夕夕");
        result.setPrice(5399);
        LogHelper.printLog("获取某夕夕上" + product + "的价格完成：5399");
        return result;
    }

    public static int getMouXixiDiscounts(String product) {
        LogHelper.printLog("获取某夕夕上" + product + "的优惠");;
        mockCostTimeOperation();
        LogHelper.printLog("获取某夕夕上" + product + "的优惠完成：-5300");
        return 5300;
    }

    private static void mockCostTimeOperation() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
