package com.lubayi.fight.apply.security.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lubayi.fight.apply.security.repository.entity.SysUser;
import com.lubayi.fight.apply.security.repository.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, SysUser> {

    public Optional<SysUser> findByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username);
        return Optional.ofNullable(this.getOne(queryWrapper));
    }

    public Optional<SysUser> findByIdNum(String IdNum) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getIdNum, IdNum);
        return Optional.ofNullable(this.getOne(queryWrapper));
    }

}
