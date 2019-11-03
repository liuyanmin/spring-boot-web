package com.lym.springboot.web.core.dto.request;

import com.lym.springboot.web.core.vo.RoleMenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("保存角色")
public class RoleSaveParam {

    @NotBlank(message = "roleName 不能为空")
    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @Valid
    @ApiModelProperty("菜单权限")
    private List<RoleMenuVo> menuPermissions;
}
