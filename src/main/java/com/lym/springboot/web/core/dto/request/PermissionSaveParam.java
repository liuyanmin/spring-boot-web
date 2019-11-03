package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("保存权限")
public class PermissionSaveParam {

    @NotBlank(message = "permissionName 不能为空")
    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;
}
