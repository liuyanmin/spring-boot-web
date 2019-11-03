package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.dto.request.PermissionListParam;
import com.lym.springboot.web.core.dto.request.PermissionSaveParam;
import com.lym.springboot.web.core.dto.request.PermissionUpdateParam;
import com.lym.springboot.web.core.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "权限管理")
@RequiresPermissions
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public ApiResult save(@Valid @RequestBody PermissionSaveParam permissionSaveParam) {
        return permissionService.save(permissionSaveParam);
    }

    @PostMapping("/list")
    @ApiOperation("列表")
    public ApiResult<PageBean<WebPermission>> list(@Valid @RequestBody PermissionListParam permissionListParam) {
        return permissionService.list(permissionListParam);
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    @ApiImplicitParam(name = "permissionId", value = "权限ID")
    public ApiResult<WebPermission> detail(@Min(value = 1, message = "permissionId 是数字且最小值为1") @RequestParam Integer permissionId) {
        return permissionService.detail(permissionId);
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ApiResult update(@Valid @RequestBody PermissionUpdateParam permissionUpdateParam) {
        return permissionService.update(permissionUpdateParam);
    }
}
