package com.lubayi.fight.apply.security.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lubayi.fight.apply.security.repository.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
