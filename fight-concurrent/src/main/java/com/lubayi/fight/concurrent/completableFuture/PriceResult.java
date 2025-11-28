package com.lubayi.fight.concurrent.completableFuture;

import lombok.Data;

/**
 * @author lubayi
 * @date 2025/11/28
 */
@Data
public class PriceResult {

    private int price;

    private int discounts;

    private int realPrice;

    private String platform;

    public PriceResult(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return
                "【平台：" + platform +
                "，原价：" + price +
                "，折扣：" + discounts +
                "，实时价：" + realPrice +
                "】";
    }
}
