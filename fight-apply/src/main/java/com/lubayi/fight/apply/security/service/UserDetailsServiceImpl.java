package com.lubayi.fight.apply.security.service;

import com.lubayi.fight.apply.security.repository.dao.UserDao;
import com.lubayi.fight.apply.security.repository.entity.LoginUser;
import com.lubayi.fight.apply.security.repository.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Author: lubayi
 * Date: 2025/11/19
 * Time: 22:40
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUser> userOptional = userDao.findByUsername(username);
        if (userOptional.isPresent()) {
            throw new RuntimeException("");
        }
        return new LoginUser(userOptional.get().getUserId(), userOptional.get());
    }

}
