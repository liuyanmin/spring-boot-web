package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.domain.WebMenu;
import com.lym.springboot.web.core.domain.WebMenuPermissionRe;
import com.lym.springboot.web.core.vo.MenuPermissionResponseVo;
import com.lym.springboot.web.core.vo.MenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Data
@Accessors(chain = true)
@ApiModel("菜单详情")
public class MenuDetailResponse {

    @ApiModelProperty("菜单详情")
    private WebMenu menu;

    @ApiModelProperty("菜单列表")
    private List<MenuVo> menuList;

    @ApiModelProperty("菜单权限列表，只有最后一级菜单返回，中间的二级菜单不返回权限列表")
    private List<MenuPermissionResponseVo> menuPermissions;

}
