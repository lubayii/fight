package com.lubayi.fight.apply.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lubayi
 * @date 2025/11/17
 */
@RestController
@RequestMapping("/aop")
public class UseController {

    @InternalAuth
    @GetMapping
    public void businessDeal() {
        System.out.println("内部调用请求执行完毕！");
    }

}
