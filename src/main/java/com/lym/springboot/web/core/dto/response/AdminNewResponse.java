package com.lym.springboot.web.core.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Accessors(chain = true)
@ApiModel("角色列表")
public class AdminNewResponse {

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("是否被选中，true-是 false-否")
    private Boolean selected;

}
