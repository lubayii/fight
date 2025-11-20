package com.lubayi.fight.apply.security.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Author: lubayi
 * Date: 2025/11/19
 * Time: 22:45
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private Long userId;

    private String tokenId;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    private SysUser user;

    public LoginUser(Long userId, SysUser user) {
        this.userId = userId;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

}
