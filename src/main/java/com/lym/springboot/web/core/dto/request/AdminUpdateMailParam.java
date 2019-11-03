package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@ApiModel("修改邮箱")
public class AdminUpdateMailParam {

    @NotNull(message = "adminId 不能为空")
    @ApiModelProperty("用户ID")
    private Integer adminId;

    @NotBlank(message = "password 不能为空")
    @ApiModelProperty("密码，rsa加密密文")
    private String password;

    @NotBlank(message = "newEmail 不能为空")
    @ApiModelProperty("新邮箱")
    private String newEmail;
}
