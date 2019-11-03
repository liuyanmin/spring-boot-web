package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.domain.WebMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@ApiModel("菜单列表")
public class MenuListResponse {

    @ApiModelProperty("菜单ID")
    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("顺序")
    private Byte sortBy;

    @ApiModelProperty("前台链接地址")
    private String frontUrl;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("启用状态，true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty("添加时间")
    private LocalDateTime addTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否有子菜单")
    private Boolean hasSubMenu;

    public MenuListResponse(WebMenu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.parentId = menu.getParentId();
        this.sortBy = menu.getSortBy();
        this.frontUrl = menu.getFrontUrl();
        this.icon = menu.getIcon();
        this.status = menu.getStatus();
        this.addTime = menu.getAddTime();
        this.updateTime = menu.getUpdateTime();
        this.hasSubMenu = false;
    }
}
