package com.lym.springboot.web.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户客户端信息对象
 * Created by liuyanmin on 2019/10/17.
 */
@Data
public class ClientInfo implements Serializable {
    private static final long serialVersionUID = 4548569636809522408L;

    /**
     * ip
     */
    private String ip;

    /**
     * ip对应的地址
     */
    private String addree;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 浏览器引擎名称
     */
    private String engineName;

    /**
     * 浏览器引擎版本
     */
    private String engineVersion;

    /**
     * 系统名称
     */
    private String osName;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 是否是手机
     */
    private boolean mobile;

    /**
     * 移动端设备名称
     */
    private String deviceName;

    /**
     * 移动端设备型号
     */
    private String deviceModel;

}
