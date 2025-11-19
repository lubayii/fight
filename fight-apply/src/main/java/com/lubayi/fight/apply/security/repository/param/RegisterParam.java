package com.lubayi.fight.apply.security.repository.param;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Data
public class RegisterParam {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String userName;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idNum;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

}
