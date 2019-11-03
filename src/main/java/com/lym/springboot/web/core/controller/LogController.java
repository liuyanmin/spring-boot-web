package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebLog;
import com.lym.springboot.web.core.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuyanmin on 2019/9/30.
 */
@Log4j
@RestController
@RequestMapping("/log")
@Api(tags = "日志管理")
public class LogController {

    @Autowired
    private ILogService logService;

    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    @RequiresPermissions
    public ApiResult<PageBean<WebLog>> list(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        PageBean<WebLog> logInfoList = logService.list(pageNum, pageSize);
        return ApiResult.ok(logInfoList);
    }
}
