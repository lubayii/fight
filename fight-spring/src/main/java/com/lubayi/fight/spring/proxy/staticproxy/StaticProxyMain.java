package com.lubayi.fight.spring.proxy.staticproxy;

import com.lubayi.fight.spring.proxy.SmsService;
import com.lubayi.fight.spring.proxy.SmsServiceImpl;

/**
 * @author lubayi
 * @date 2025/12/12
 */
public class StaticProxyMain {

    public static void main(String[] args) {
        SmsService smsService = new SmsServiceImpl();
        SmsProxy smsProxy = new SmsProxy(smsService);
        smsProxy.send("java");
    }

}
