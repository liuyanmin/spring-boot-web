package com.lym.springboot.web.core.dto.request;

import com.lym.springboot.web.core.vo.MenuPermissionParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("修改菜单")
public class MenuUpdateParam {

    @NotNull(message = "menuId 不能为空")
    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @NotBlank(message = "menuName 不能为空")
    @ApiModelProperty("菜单名称")
    private String menuName;

    @NotNull(message = "parentId 不能为空")
    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @Range(min = 0, max = 127, message = "sortBy 范围在0-127之间")
    @ApiModelProperty("顺序")
    private Byte sortBy;

    @ApiModelProperty("前台链接地址")
    private String frontUrl;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @Valid
    @ApiModelProperty("菜单权限")
    private List<MenuPermissionParamVo> menuPermissions;

}
