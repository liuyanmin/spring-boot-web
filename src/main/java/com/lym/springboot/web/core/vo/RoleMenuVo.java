package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
public class RoleMenuVo {

    @NotNull(message = "menuId 不能为空")
    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("权限ID列表")
    private List<Integer> permissionIds;
}
