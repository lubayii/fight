package com.lubayi.fight.springsecurity.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author lubayi
 * @date 2025/11/25
 */
@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handleChat(Principal principal, Info info) {
        if (principal.getName().equals("bob")) {
            messagingTemplate.convertAndSendToUser("alice",
                    "/queue/notification", principal.getName() + " send message to you: "
                            + info.getInfo());
        } else {
            messagingTemplate.convertAndSendToUser("bob",
                    "/queue/notification", principal.getName() + " send message to you: "
                            + info.getInfo());
        }
    }
}
