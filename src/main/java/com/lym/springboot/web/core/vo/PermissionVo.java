package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by liuyanmin on 2019/10/19.
 */
@Data
@Accessors(chain = true)
public class PermissionVo {

    @ApiModelProperty("权限ID")
    private Integer permissionId;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("是否被选中，true-是 false-否")
    private Boolean selected;
}
