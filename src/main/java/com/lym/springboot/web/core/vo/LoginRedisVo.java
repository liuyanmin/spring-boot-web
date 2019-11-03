package com.lym.springboot.web.core.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Accessors(chain = true)
public class LoginRedisVo implements Serializable {
    private static final long serialVersionUID = -3827294794824080256L;

    /**
     * 用户信息
     */
    private AdminLoginVo adminLoginVo;

    /**
     * 用户客户端信息
     */
    private ClientInfo clientInfo;

    /**
     * jwt token
     */
    private String token;

    /**
     * 登录时间，单位：毫秒
     */
    private Long loginTime;

    /**
     * 最后操作时间，单位：毫秒
     */
    private Long lastOperationTime;

}
