package com.lym.springboot.web.common.interceptor;

import com.lym.springboot.web.common.api.ApiCode;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.CommonConstant;
import com.lym.springboot.web.common.constant.CommonRedisKey;
import com.lym.springboot.web.config.redis.RedisTemplate;
import com.lym.springboot.web.config.properties.LoginProperties;
import com.lym.springboot.web.util.jwt.JwtUtil;
import com.lym.springboot.web.core.vo.LoginRedisVo;
import com.lym.springboot.web.util.HttpServletResponseUtil;
import com.lym.springboot.web.util.LoginUtil;
import com.lym.springboot.web.util.ObjectMapperUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断jwt token 拦截器
 * 流程：
 *      1、登录生成jwt token，不设置token的过期时间
 *      2、jwt 拦截器，从header 中获取token
 *      3、校验token 是否有效
 *      4、获取token的最后访问时间，>X分钟，token失效，重新登录
 *      5、若单账号登录，校验token是否为当前用户最后一次登录的，若不是，则重新登录
 *      6、用户Id存入request的请求参数中，业务接口可以直接获取
 *
 * Created by liuyanmin on 2019/10/17.
 */
@Log4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LoginProperties loginProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果访问的不是控制器,则跳出,继续执行下一个拦截器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取token
        String token = LoginUtil.getToken(request);

        // 如果token为空，则s输出提示并返回
        if (StringUtils.isBlank(token)) {
            ApiResult apiResult = ApiResult.fail(ApiCode.UNAUTHORIZED);
            log.error("token is empty");
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }

        log.debug("token: " + token);

        // 验证token是否有效
        Jws<Claims> jws = JwtUtil.verify(token);
        log.debug("token verify: " + jws);

        if (jws == null) {
            ApiResult apiResult = ApiResult.fail(ApiCode.UNAUTHORIZED);
            log.error("token verify failed");
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }


        String loginDataStr;
        Integer adminId = (Integer) jws.getBody().get(CommonConstant.ADMIN_ID);
        String key = String.format(CommonRedisKey.LOGIN_TOKEN, adminId);
        if (loginProperties.getMulti()) {
            String tokenMd5 = DigestUtils.md5Hex(token);
            loginDataStr = redisTemplate.hget(key, tokenMd5);
        } else {
            loginDataStr = redisTemplate.get(key);
        }

        if (StringUtils.isBlank(loginDataStr)) {
            ApiResult apiResult = ApiResult.fail(ApiCode.UNAUTHORIZED);
            log.error("登录信息不存在");
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }

        LoginRedisVo loginRedisVo = ObjectMapperUtil.toObject(loginDataStr, LoginRedisVo.class);
        if (loginRedisVo == null) {
            ApiResult apiResult = ApiResult.fail(ApiCode.UNAUTHORIZED);
            log.error("登录信息反序列化失败");
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }

        // 校验用户操作的间隔时间
        Long lastOperationTime = loginRedisVo.getLastOperationTime();
        long diff = (lastOperationTime + loginProperties.getExpireMinutes() * 60 * 1000) - System.currentTimeMillis();
        if (diff < 0) {
            ApiResult apiResult = ApiResult.fail(ApiCode.UNAUTHORIZED);
            log.error("token 过期");
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }

        // 刷新用户最后操作时间
        loginRedisVo.setLastOperationTime(System.currentTimeMillis());
        loginDataStr = ObjectMapperUtil.toJsonString(loginRedisVo);
        if (loginProperties.getMulti()) {
            String tokenMd5 = DigestUtils.md5Hex(token);
            redisTemplate.hset(key, tokenMd5, loginDataStr);
        } else {
            redisTemplate.set(key, loginDataStr);
        }

        // 存储jws对象到request对象中
        request.setAttribute("jws", jws);

        // 存储登录用户ID
        request.setAttribute(CommonConstant.ADMIN_ID, adminId);

        log.debug("token verify success");
        return true;
    }

}
