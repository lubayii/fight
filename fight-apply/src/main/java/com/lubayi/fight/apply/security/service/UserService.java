package com.lubayi.fight.apply.security.service;

import com.lubayi.fight.apply.security.repository.dao.RoleDao;
import com.lubayi.fight.apply.security.repository.dao.UserDao;
import com.lubayi.fight.apply.security.repository.dao.UserRoleDao;
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

    public void loginUser(LoginParam loginParam) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

    }

}
