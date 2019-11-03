package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Accessors(chain = true)
@ApiModel("用户列表")
public class AdminListParam {

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("筛选，登录名")
    private String username;

    @ApiModelProperty("筛选，姓名")
    private String fullname;

    @ApiModelProperty("筛选，邮箱")
    private String email;

    @ApiModelProperty("筛选，性别：0 未知， 1男， 2 女")
    private Byte gender;

    @ApiModelProperty("筛选，手机号")
    private String mobile;

    @ApiModelProperty("筛选，启用状态，true-启用 false-禁用 ")
    private Boolean status;

    @ApiModelProperty("筛选，是否超级管理员，true-是 false-否")
    private Boolean superAdmin;

    @ApiModelProperty("排序，升序-asc 降序-desc，多列之间用逗号隔开，例如：'id asc, name desc'")
    private String orderBy;
}
