package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Data
@ApiModel("保存系统参数")
public class SystemSaveParam {

    @NotBlank(message = "keyName 不能为空")
    @ApiModelProperty("名称")
    private String keyName;

    @NotBlank(message = "keyValue 不能为空")
    @ApiModelProperty("值")
    private String keyValue;

    @ApiModelProperty("描述")
    private String description;

}
