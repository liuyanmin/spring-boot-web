package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebRole;
import com.lym.springboot.web.core.dto.request.RoleListParam;
import com.lym.springboot.web.core.dto.request.RoleSaveParam;
import com.lym.springboot.web.core.dto.request.RoleUpdateParam;
import com.lym.springboot.web.core.dto.response.RoleDetailResponse;
import com.lym.springboot.web.core.service.IRoleService;
import com.lym.springboot.web.core.vo.RoleMenuDetailResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
@RequiresPermissions
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public ApiResult save(@Valid @RequestBody RoleSaveParam roleSaveParam) {
        return roleService.save(roleSaveParam);
    }

    @PostMapping("/list")
    @ApiOperation("列表")
    public ApiResult<PageBean<WebRole>> list(@RequestBody RoleListParam roleListParam) {
        return roleService.list(roleListParam);
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    @ApiImplicitParam(name = "roleId", value = "角色ID")
    public ApiResult<RoleDetailResponse> detail(@RequestParam Integer roleId) {
        return roleService.detail(roleId);
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ApiResult update(@Valid @RequestBody RoleUpdateParam roleUpdateParam) {
        return roleService.update(roleUpdateParam);
    }

    @GetMapping("/new")
    @ApiOperation("新建")
    public ApiResult<RoleMenuDetailResponseVo> newPage() {
        return roleService.newPage();
    }
}
