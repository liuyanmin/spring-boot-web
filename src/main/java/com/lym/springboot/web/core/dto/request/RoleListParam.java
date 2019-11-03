package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("角色列表")
public class RoleListParam {

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("筛选，角色名称")
    private String roleName;

    @ApiModelProperty("筛选，启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("排序")
    private String orderBy;

}
