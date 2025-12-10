package com.lubayi.fight.apply.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author: lubayi
 * Date: 2025/12/11
 * Time: 07:13
 */
@Service
@RequiredArgsConstructor
public class TransactionPropagationExample {

    private final User1Service user1Service;

    private final User2Service user2Service;

    public void notransaction_exception_required_required() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequired(user2);

        throw new RuntimeException();
    }

    public void notransaction_required_required_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiredException(user2);
    }

}
