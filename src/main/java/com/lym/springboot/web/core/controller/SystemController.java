package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebSystem;
import com.lym.springboot.web.core.dto.request.SystemListParam;
import com.lym.springboot.web.core.dto.request.SystemSaveParam;
import com.lym.springboot.web.core.dto.request.SystemUpdateParam;
import com.lym.springboot.web.core.service.ISystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@RestController
@RequestMapping("/system")
@Api(tags = "系统参数管理")
public class SystemController {

    @Autowired
    private ISystemService systemService;

    @PostMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions
    public ApiResult<PageBean<WebSystem>> list(@RequestBody SystemListParam systemListParam) {
        return systemService.list(systemListParam);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions
    public ApiResult save(@Valid @RequestBody SystemSaveParam systemSaveParam) {
        return systemService.save(systemSaveParam);
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    @RequiresPermissions
    @ApiImplicitParam(name = "systemId", value = "系统参数ID")
    public ApiResult<WebSystem> detail(@RequestParam Integer systemId) {
        return systemService.detail(systemId);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions
    public ApiResult update(@Valid @RequestBody SystemUpdateParam systemUpdateParam) {
        return systemService.update(systemUpdateParam);
    }

    @RequestMapping("/")
    @ApiOperation(value = "项目信息接口")
    public Object index() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("projectName", "spring-boot-web");
        info.put("projectVersion", "1.0.0");
        info.put("ahthor", "liuyanmin");
        info.put("email", "liuyanmin@163.com");
        info.put("contact", "https://github.com/liuyanmin");
        return ApiResult.ok(info);
    }

    @GetMapping("/status")
    @ApiOperation(value = "服务监控接口")
    public Object status() {
        return ApiResult.ok();
    }
}
