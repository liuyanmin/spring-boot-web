package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "管理员登录信息")
public class AdminLoginVo implements Serializable {
    private static final long serialVersionUID = -8021229437255205996L;

    @ApiModelProperty("管理员用户ID")
    private Integer adminId;

    @ApiModelProperty("登录名称")
    private String username;

    @ApiModelProperty("头像url")
    private String avatar;

}
