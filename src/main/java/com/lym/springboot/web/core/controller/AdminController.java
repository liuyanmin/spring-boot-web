package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dto.request.*;
import com.lym.springboot.web.core.dto.response.AdminDetailResponse;
import com.lym.springboot.web.core.dto.response.AdminListResponse;
import com.lym.springboot.web.core.dto.response.AdminNewResponse;
import com.lym.springboot.web.core.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "用户管理")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @PostMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions
    public ApiResult<PageBean<AdminListResponse>> list(@RequestBody AdminListParam adminListParam) {
        return adminService.list(adminListParam);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions
    public ApiResult save(@Valid @RequestBody AdminSaveParam adminSaveParam) {
        return adminService.save(adminSaveParam);
    }

    @GetMapping("/new")
    @ApiOperation("新建")
    @RequiresPermissions
    public ApiResult<AdminNewResponse> newPage() {
        return adminService.newPage();
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    @RequiresPermissions
    @ApiImplicitParam(name = "adminId", value = "用户ID")
    public ApiResult<AdminDetailResponse> detail(@RequestParam Integer adminId) {
        return adminService.detail(adminId);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions
    public ApiResult update(@Valid @RequestBody AdminUpdateParam adminUpdateParam) {
        return adminService.update(adminUpdateParam);
    }

    @GetMapping("/find/password")
    @ApiOperation("找回密码")
    @ApiImplicitParam(name = "username", value = "登录名")
    public ApiResult findPassword(@RequestParam String username) {
        return adminService.findPassword(username);
    }

    @GetMapping("/confirm/reset/password")
    @ApiOperation("确认找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户登录名"),
            @ApiImplicitParam(name = "c", value = "认证码")
    })
    public ApiResult findPasswordConfirm(@RequestParam String u, @RequestParam String c) {
        return adminService.confirmResetPassword(u, c);
    }

    @PostMapping("/update/password")
    @ApiOperation("修改密码")
    @RequiresPermissions
    public ApiResult updatePassword(@Valid @RequestBody AdminUpdatePwdParam adminUpdatePwdParam) {
        return adminService.updatePassword(adminUpdatePwdParam);
    }

    @PostMapping("/update/mail")
    @ApiOperation("修改邮箱")
    @RequiresPermissions
    public ApiResult updateMail(@Valid @RequestBody AdminUpdateMailParam adminUpdateMailParam) {
        return adminService.updateMail(adminUpdateMailParam);
    }
}
