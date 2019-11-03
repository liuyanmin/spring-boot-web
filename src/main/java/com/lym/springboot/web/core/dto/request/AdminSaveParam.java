package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@ApiModel("保存用户")
public class AdminSaveParam {

    @NotBlank(message = "username 不能为空")
    @ApiModelProperty("登录名")
    private String username;

    @ApiModelProperty("姓名")
    private String fullname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别：0 未知， 1男， 2 女")
    private Byte gender;

    @ApiModelProperty("年龄")
    private Byte age;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("是否超级管理员，true-是 false-否")
    private Boolean superAdmin;
}
