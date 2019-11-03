package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
public class MenuPermissionParamVo {

    @NotNull(message = "permissionId 不能为空")
    @ApiModelProperty("权限ID")
    private Integer permissionId;

    @ApiModelProperty("后台接口uri")
    private String apiUri;

    @ApiModelProperty("前台url，若没有为空")
    private String frontUrl;

}
