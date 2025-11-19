package com.lubayi.fight.apply.security.service;

import com.lubayi.fight.apply.security.repository.dao.UserDao;
import com.lubayi.fight.apply.security.repository.entity.SysUser;
import com.lubayi.fight.apply.security.repository.param.RegisterParam;
import com.lubayi.fight.apply.security.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void registerUser(RegisterParam registerParam) {
        if (userDao.findByUsername(registerParam.getUserName()).isPresent()) {
            throw new RuntimeException();
        }
        if (userDao.findByIdNum(registerParam.getIdNum()).isPresent()) {
            throw new RuntimeException();
        }

        SysUser user = new SysUser();
        user.setUserName(registerParam.getUserName());
        user.setPhonenumber(registerParam.getUserName());
        user.setName(registerParam.getName());
        user.setIdNum(registerParam.getIdNum());
        user.setEmail(registerParam.getEmail());
        user.setPassword(PasswordUtil.encode(registerParam.getPassword()));
        userDao.save(user);
    }

    public void getSmsCode(String phone) {
        if (userDao.findByUsername(phone).isPresent()) {
            throw new RuntimeException();
        }

    }

}
