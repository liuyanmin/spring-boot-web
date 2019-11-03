package com.lym.springboot.web.core.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Data
@ApiModel("validation测试类")
public class ValidationVo {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @Size(min = 6, max = 11, message = "手机号长度必须在6~11之间")
    private String phone;

}
