package com.lym.springboot.web.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.lym.springboot.web.common.api.ApiCode;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.CommonRedisKey;
import com.lym.springboot.web.config.redis.RedisTemplate;
import com.lym.springboot.web.core.dao.AdminDao;
import com.lym.springboot.web.core.domain.WebAdmin;
import com.lym.springboot.web.core.dto.response.LoginResponse;
import com.lym.springboot.web.core.dto.response.UserMenuListResponse;
import com.lym.springboot.web.core.service.IAdminService;
import com.lym.springboot.web.config.properties.LoginProperties;
import com.lym.springboot.web.core.dto.request.LoginParam;
import com.lym.springboot.web.core.service.ILoginService;
import com.lym.springboot.web.util.jwt.JwtUtil;
import com.lym.springboot.web.core.vo.AdminLoginVo;
import com.lym.springboot.web.core.vo.ClientInfo;
import com.lym.springboot.web.core.vo.LoginRedisVo;
import com.lym.springboot.web.util.*;
import com.lym.springboot.web.util.encrypt.RsaDecryptUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Log4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private IAdminService adminService;

    @Override
    public ApiResult<LoginResponse> login(LoginParam loginParam) {
        String password = decryptPassword(loginParam.getPassword());
        if (StringUtils.isEmpty(password)) {
            log.error("加密密码解密失败");
            return ApiResult.fail(ApiCode.ADMIN_USERNAME_OR_PWD_ERROR);
        }

        String passwordMd5 = Md5Util.md5(password);
        WebAdmin admin = adminDao.getByUserAndPwd(loginParam.getUsername(), passwordMd5);
        if (admin == null) {
            log.error("用户名或密码错误");
            return ApiResult.fail(ApiCode.ADMIN_USERNAME_OR_PWD_ERROR);
        }

        if (!admin.getStatus()) {
            log.error("账号被禁用");
            return ApiResult.fail(ApiCode.ADMIN_FORBIDDEN);
        }

        AdminLoginVo adminLoginVo = new AdminLoginVo()
                .setAdminId(admin.getId())
                .setUsername(admin.getUsername())
                .setAvatar(admin.getAvatar());

        // 创建token
        String token = JwtUtil.create(admin.getId());
        loginRedis(adminLoginVo, token);

        // 更新最后登录时间
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(IpUtil.getRequestIp());
        adminDao.updateById(admin);

        // 获取用户菜单
        ApiResult apiResult = adminService.userMenuList(admin.getId());
        List<UserMenuListResponse> menuList = null;
        if (apiResult.getCode() == ApiCode.SUCCESS.getCode()) {
            menuList = (List<UserMenuListResponse>) apiResult.getData();
        }

        LoginResponse response = new LoginResponse()
                .setToken(token)
                .setAdminLoginVo(adminLoginVo)
                .setMenuList(menuList);

        return ApiResult.ok(response);
    }

    @Override
    public ApiResult logout(Integer adminId, String token) {
        String tokenMd5 = DigestUtils.md5Hex(token);
        String key = String.format(CommonRedisKey.LOGIN_TOKEN, adminId);
        if (loginProperties.getMulti()) { // 支持多端登录
            redisTemplate.hdel(key, tokenMd5);
        } else { // 不支持
            redisTemplate.del(key);
        }
        return ApiResult.ok();
    }

    private void loginRedis(AdminLoginVo adminLoginVo, String token) {
        // token md5,方便使用
        String tokenMd5 = DigestUtils.md5Hex(token);

        // 获取用户客户端信息
        HttpServletRequest request = HttpServletRequestUtil.getRequest();
        String userAgent = request.getHeader("User-Agent");
        ClientInfo clientInfo = ClientInfoUtil.get(userAgent);
        log.debug("clientInfo : " + JSON.toJSONString(clientInfo, true));

        // 缓存登录信息
        LoginRedisVo loginRedisVo = new LoginRedisVo()
                .setAdminLoginVo(adminLoginVo)
                .setClientInfo(clientInfo)
                .setToken(token)
                .setLoginTime(System.currentTimeMillis())
                .setLastOperationTime(System.currentTimeMillis());

        // 存入redis
        String loginData = ObjectMapperUtil.toJsonString(loginRedisVo);
        Integer adminId = adminLoginVo.getAdminId();

        String key = String.format(CommonRedisKey.LOGIN_TOKEN, adminId);
        if (loginProperties.getMulti()) { // 支持多端登录
            redisTemplate.hset(key, tokenMd5, loginData);
        } else { // 不支持
            redisTemplate.set(key, loginData);
        }
    }

    @Override
    public String decryptPassword(String ciphertext) {
        // 解密登录密码
        try {
            // 判断密码是否在黑名单中
            boolean exists = redisTemplate.sismember(CommonRedisKey.PASSWORD_BLACKLIST, ciphertext);
            if (exists) {
                log.error("登录异常，加密密码被重复使用");
                return null;
            }
            String password = RsaDecryptUtil.decrypt(ciphertext);
            // 解密成功，将密码密文写入redis中
            redisTemplate.sadd(CommonRedisKey.PASSWORD_BLACKLIST, ciphertext);
            return password;
        } catch (Exception e) {
            log.error("密码解密异常: ", e);
            return null;
        }
    }
}
