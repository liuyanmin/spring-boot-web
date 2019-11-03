package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Api("登录参数")
public class LoginParam {

    @NotBlank(message = "username 不能为空")
    @ApiModelProperty("账号")
    private String username;

    @NotBlank(message = "password 不能为空")
    @ApiModelProperty("密码，Rsa加密密文")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

}
