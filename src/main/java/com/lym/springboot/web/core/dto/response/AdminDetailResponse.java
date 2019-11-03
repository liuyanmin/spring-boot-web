package com.lym.springboot.web.core.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Accessors(chain = true)
@ApiModel("用户详情")
public class AdminDetailResponse {

    @ApiModelProperty("用户ID")
    private Integer adminId;

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

    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("是否超级管理员，1-是 0-否")
    private Boolean superAdmin;

    @ApiModelProperty("添加时间")
    private LocalDateTime addTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("角色列表")
    private List<AdminNewResponse> roles;

}
