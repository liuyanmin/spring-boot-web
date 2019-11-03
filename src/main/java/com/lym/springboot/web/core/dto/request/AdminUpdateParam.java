package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
public class AdminUpdateParam {

    @NotNull(message = "adminId 不能为空")
    @ApiModelProperty("用户ID")
    private Integer adminId;

    @ApiModelProperty("姓名")
    private String fullname;

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

    @ApiModelProperty("是否超级管理员，1-是 0-否")
    private Boolean superAdmin;
}
