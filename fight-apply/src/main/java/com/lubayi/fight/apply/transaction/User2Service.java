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
public class User2Service {

    private final User2Mapper user2Mapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User2 user) {
        user2Mapper.insert(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredException(User2 user) {
        user2Mapper.insert(user);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User2 user) {
        user2Mapper.insert(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(User2 user) {
        user2Mapper.insert(user);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User2 user) {
        user2Mapper.insert(user);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addNestedException(User2 user) {
        user2Mapper.insert(user);
        throw new RuntimeException();
    }

}
