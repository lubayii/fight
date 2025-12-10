package com.lubayi.fight.apply.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: lubayi
 * Date: 2025/12/11
 * Time: 06:55
 */
@Service
@RequiredArgsConstructor
public class User1Service {

    private final User1Mapper user1Mapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User1 user) {
        user1Mapper.insert(user);
    }

}
