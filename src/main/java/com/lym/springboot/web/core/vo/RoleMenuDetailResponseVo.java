package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by liuyanmin on 2019/10/19.
 */
@Data
@Accessors(chain = true)
@ApiModel("角色菜单列表")
public class RoleMenuDetailResponseVo {

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("是否被选中，true-是 false-否")
    private Boolean selected;

    @ApiModelProperty("子菜单")
    private List<RoleMenuDetailResponseVo> childrens;

    @ApiModelProperty("权限列表")
    private List<PermissionVo> permissions;
}
