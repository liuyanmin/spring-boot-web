package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.core.dto.request.LoginParam;
import com.lym.springboot.web.core.dto.response.LoginResponse;

/**
 * Created by liuyanmin on 2019/10/17.
 */
public interface ILoginService {

    /**
     * 登录
     * @param loginParam
     * @return
     */
    ApiResult<LoginResponse> login(LoginParam loginParam);

    /**
     * 登出
     * @param adminId
     * @return
     */
    ApiResult logout(Integer adminId, String token);

    /**
     * 解密Rsa密文
     */
    String decryptPassword(String ciphertext);

}
