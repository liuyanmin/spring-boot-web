package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@Accessors(chain = true)
@ApiModel("权限列表")
public class MenuPermissionResponseVo {

    @ApiModelProperty("权限ID")
    private Integer permissionId;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("后台接口uri")
    private String apiUri;

    @ApiModelProperty("前端链接地址")
    private String frontUrl;

    @ApiModelProperty("是否被选中，true-是 false-否")
    private Boolean selected;

}
