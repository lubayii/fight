package com.lubayi.fight.apply.security.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lubayi.fight.apply.security.repository.entity.SysRole;
import com.lubayi.fight.apply.security.repository.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, SysRole> {

    public SysRole findByRoleKey(String roleKey) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleKey, roleKey);
        return this.getOne(queryWrapper);
    }

}
