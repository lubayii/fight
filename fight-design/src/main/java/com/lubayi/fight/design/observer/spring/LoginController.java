package com.lubayi.fight.design.observer.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lubayi
 * @date 2025/11/5
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final ApplicationContext context;

    private final ApplicationEventPublisher publisher;

    private final ApplicationEventMulticaster multicaster;

}
