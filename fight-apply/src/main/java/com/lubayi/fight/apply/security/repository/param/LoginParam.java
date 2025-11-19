package com.lubayi.fight.apply.security.repository.param;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Data
public class LoginParam {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String userName;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

}
