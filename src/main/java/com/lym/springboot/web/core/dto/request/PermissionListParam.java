package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("权限列表")
public class PermissionListParam {

    @ApiModelProperty("页数，默认值为1")
    private Integer pageNum;

    @ApiModelProperty("每页数量，默认值为10")
    private Integer pageSize;

    @ApiModelProperty("筛选，权限名称")
    private String permissionName;

    @ApiModelProperty("筛选，启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("排序，asc-升序 desc-降序，多列排序用逗号隔开，示例: 'permissionName asc,status desc'")
    private String orderBy;
}
