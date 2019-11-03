package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Accessors(chain = true)
public class MenuVo {

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("子菜单")
    private List<MenuVo> childrens;

}
