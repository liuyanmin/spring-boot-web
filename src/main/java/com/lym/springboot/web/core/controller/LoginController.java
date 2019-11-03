package com.lym.springboot.web.core.controller;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.core.dto.request.LoginParam;
import com.lym.springboot.web.core.dto.response.LoginResponse;
import com.lym.springboot.web.core.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Log4j
@RestController
@RequestMapping("")
@Api(tags = "登录管理")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ApiResult<LoginResponse> login(@Validated @RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public ApiResult logout(@ApiIgnore @RequestAttribute Integer userId, @RequestHeader String token) {
        return loginService.logout(userId, token);
    }

}
