package com.lym.springboot.web.util.jwt;

import com.lym.springboot.web.common.constant.CommonConstant;
import com.lym.springboot.web.config.properties.JwtProperties;
import com.lym.springboot.web.util.UUIDUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt 工具类
 * Created by liuyanmin on 2019/10/16.
 */
@Log4j
@Component
public class JwtUtil {

    private static final String UTF8 = "UTF-8";
    private static JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        JwtUtil.jwtProperties = jwtProperties;
    }

    public static String create(Integer userId) {
        return create(userId, null, null);
    }

    public static String create() {
        return create(null, null, null);
    }

    /**
     * @param adminId 登录用户ID
     * @param headerClaims 头部信息
     * @param payloadClaims 负载
     * @return
     */
    public static String create(Integer adminId, Map<String, Object> headerClaims, Map<String, Object> payloadClaims) {

        if (MapUtils.isEmpty(headerClaims)) {
            headerClaims = new HashMap<>();
        }

        if (MapUtils.isEmpty(payloadClaims)) {
            payloadClaims = new HashMap<>();
        }

        Header header = new DefaultJwsHeader();
        header.setContentType(Header.CONTENT_TYPE);
        header.setType(Header.JWT_TYPE);
        headerClaims.putAll(header);

        payloadClaims.put(CommonConstant.ADMIN_ID, adminId);

        try {
            String token = Jwts.builder()
                    .setHeader(headerClaims)
                    .setClaims(payloadClaims)
                    .setId(UUIDUtil.getUUID())
                    .setIssuer(jwtProperties.getIssuer())
                    .setSubject(jwtProperties.getSubject())
                    .setAudience(jwtProperties.getAudience())
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes(UTF8))
                    .compact();
            return token;
        } catch (Exception e) {
            log.error("create token exception: ", e);
        }
        return null;
    }

    /**
     * 验证jwt token
     * @param token
     * @return
     */
    public static Jws<Claims> verify(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes(UTF8))
                    .parseClaimsJws(token);
            return jws;
        } catch (Exception e) {
            log.error("verify token exception: ", e);
        }
        return null;
    }

}
