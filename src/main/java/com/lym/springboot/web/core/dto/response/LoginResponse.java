package com.lym.springboot.web.core.dto.response;

import com.lym.springboot.web.core.vo.AdminLoginVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "登录")
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = -1087721857307742219L;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户信息")
    private AdminLoginVo adminLoginVo;

    @ApiModelProperty("菜单列表")
    private List<UserMenuListResponse> menuList;
}
