package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.core.dto.request.MenuSaveParam;
import com.lym.springboot.web.core.dto.request.MenuUpdateParam;
import com.lym.springboot.web.core.dto.response.MenuDetailResponse;
import com.lym.springboot.web.core.dto.response.MenuListResponse;
import com.lym.springboot.web.core.dto.response.MenuNewResponse;
import com.lym.springboot.web.core.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
@RequiresPermissions
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public ApiResult save(@Valid @RequestBody MenuSaveParam menuSaveParam) {
        return menuService.save(menuSaveParam);
    }

    @GetMapping("/list")
    @ApiOperation("列表")
    @ApiImplicitParam(name = "menuId", value = "查询指定菜单下的子菜单，查询所有一级菜单时传0")
    public ApiResult<List<MenuListResponse>> list(@RequestParam(required = false, defaultValue = "0") Integer menuId) {
        return menuService.list(menuId);
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    @ApiImplicitParam(name = "menuId", value = "菜单ID")
    public ApiResult<MenuDetailResponse> detail(@RequestParam Integer menuId) {
        return menuService.detail(menuId);
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ApiResult update(@Valid @RequestBody MenuUpdateParam menuUpdateParam) {
        return menuService.update(menuUpdateParam);
    }

    @GetMapping("/new")
    @ApiOperation("新建")
    public ApiResult<MenuNewResponse> newPage() {
        return menuService.newPage();
    }

}
