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
@ApiModel("修改密码")
public class AdminUpdatePwdParam {

    @NotNull(message = "adminId 不能为空")
    @ApiModelProperty("用户ID")
    private Integer adminId;

    @NotBlank(message = "oldPassword 不能为空")
    @ApiModelProperty("旧密码，rsa加密密文")
    private String oldPassword;

    @NotBlank(message = "newPassword 不能为空")
    @ApiModelProperty("新密码，rsa加密密文")
    private String newPassword;

}
