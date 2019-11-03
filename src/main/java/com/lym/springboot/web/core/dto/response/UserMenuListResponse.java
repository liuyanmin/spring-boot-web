package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.vo.MenuPermissionResponseVo;
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
@ApiModel("用户菜单列表")
public class UserMenuListResponse {

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("子菜单")
    private List<UserMenuListResponse> childrens;

    @ApiModelProperty("权限列表")
    private List<MenuPermissionResponseVo> permissions;
}
