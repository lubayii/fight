package com.lubayi.fight.apply.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Author: lubayi
 * Date: 2025/11/6
 * Time: 21:22
 */
@Service
@RequiredArgsConstructor
public class BusinessService {

    private final ApplicationContext applicationContext;

    public void deal() {
        System.out.println("===业务逻辑处理完毕===");
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName("给钱活动");
        applicationContext.publishEvent(new MessageEvent(projectInfo));
        System.out.println("===方法执行完毕===");
    }

}
