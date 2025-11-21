package com.lubayi.fight.apply.security.service;

import com.lubayi.fight.apply.security.repository.dao.RoleDao;
import com.lubayi.fight.apply.security.repository.dao.UserDao;
import com.lubayi.fight.apply.security.repository.dao.UserRoleDao;
import com.lubayi.fight.apply.security.repository.entity.LoginUser;
import com.lubayi.fight.apply.security.repository.entity.SysRole;
import com.lubayi.fight.apply.security.repository.entity.SysUser;
import com.lubayi.fight.apply.security.repository.entity.SysUserRole;
import com.lubayi.fight.apply.security.repository.param.LoginParam;
import com.lubayi.fight.apply.security.repository.param.RegisterParam;
import com.lubayi.fight.apply.security.util.PasswordUtil;
import com.lubayi.fight.apply.security.util.SmsCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final UserRoleDao userRoleDao;

    private final SmsCodeUtil smsCodeUtil;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Transactional
    public void registerUser(RegisterParam registerParam) {
        if (userDao.findByUsername(registerParam.getUserName()).isPresent()) {
            throw new RuntimeException();
        }
        if (userDao.findByIdNum(registerParam.getIdNum()).isPresent()) {
            throw new RuntimeException();
        }
        if (!smsCodeUtil.verifyCode(registerParam.getUserName(), registerParam.getSmsCode())) {
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

        String roleKey = "externalOrdinaryUser";
        SysRole role = roleDao.findByRoleKey(roleKey);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleDao.save(userRole);
    }

    public void getSmsCode(String phone) {
        if (userDao.findByUsername(phone).isPresent()) {
            throw new RuntimeException();
        }
        String smsCode = SmsCodeUtil.generateSecureSixDigitCode();
        // ... 调用发短信接口发送短信验证码，发送成功后将短信验证码缓存到redis中
        smsCodeUtil.cacheSmsCode(phone, smsCode);
    }

    public String loginUser(LoginParam loginParam) {
        // 封装 Authentication 对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword());
        // 通过 AuthenticationManager 的 authenticate 方法进行用户认证
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 在 Authentication 中获取用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }

}
