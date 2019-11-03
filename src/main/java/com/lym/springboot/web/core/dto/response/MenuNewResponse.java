package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.vo.MenuPermissionResponseVo;
import com.lym.springboot.web.core.vo.MenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Accessors(chain = true)
@ApiModel("新建菜单")
public class MenuNewResponse {

    @ApiModelProperty("菜单列表")
    private List<MenuVo> menuList;

    @ApiModelProperty("菜单权限列表")
    private List<MenuPermissionResponseVo> menuPermissions;

}
