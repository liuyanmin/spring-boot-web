package com.lym.springboot.web.common.constant;

/**
 * Created by liuyanmin on 2019/10/17.
 */
public interface CommonRedisKey {

    /**
     * 系统登录token
     */
    String LOGIN_TOKEN = "login:token:%s";

    /**
     * 找回密码
     */
    String FIND_PASSWORD = "login:find:password:%s";

    /**
     * 登录密码黑名单
     */
    String PASSWORD_BLACKLIST = "login:password:blacklist";

}
