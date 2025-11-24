package com.lubayi.fight.apply.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * Author: lubayi
 * Date: 2025/11/24
 * Time: 05:52
 */
@RestController
public class GreetingController {

    // 使用 MessageMapping 注解来标识所有发送到 "/hello" 这个destination 的消息，都会被路由到这个方法进行处理
    // 使用 SendTo 注解来标识这个方法返回的结果，都会被发送到它指定的 destination，"/topic/greetings"
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    // 传入的参数 HelloMessage 为客户端发送过来的消息，是自动绑定的
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");  // 根据传入的信息，返回一个欢迎消息
    }

}
