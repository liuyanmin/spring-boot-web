package com.lym.springboot.web.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备信息
 * Created by liuyanmin on 2019/10/17.
 */
@Data
public class DeviceInfo implements Serializable {
    private static final long serialVersionUID = -8095076379720468693L;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备型号
     */
    private String model;

}
