package com.lym.springboot.web.util;

import com.lym.springboot.web.common.constant.CommonConstant;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录工具类
 * Created by liuyanmin on 2019/10/17.
 */
@Log4j
public class LoginUtil {

    /**
     * 从请求头或者请求参数中
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        // 从请求头中获取token
        String token = request.getHeader(CommonConstant.TOKEN);
        if (StringUtils.isBlank(token)){
            // 从请求参数中获取token
            token = request.getParameter(CommonConstant.TOKEN);
        }
        return token;
    }

}
