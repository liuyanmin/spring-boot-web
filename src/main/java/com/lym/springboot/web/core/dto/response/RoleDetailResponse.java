package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.vo.RoleMenuDetailResponseVo;
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
@ApiModel("角色详情")
public class RoleDetailResponse {

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("菜单列表")
    private List<RoleMenuDetailResponseVo> menuList;
}
