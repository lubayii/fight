package com.lubayi.fight.apply.transaction;

import org.apache.ibatis.annotations.Mapper;

/**
 * Author: lubayi
 * Date: 2025/12/11
 * Time: 06:53
 */
@Mapper
public interface User2Mapper {

    int insert(User2 record);

    User2 selectByPrimaryKey(Integer id);

}
