package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Data
@ApiModel("修改系统参数")
public class SystemUpdateParam {

    @NotNull(message = "id 不能为空")
    @ApiModelProperty("参数ID")
    private Integer id;

    @NotBlank(message = "keyName 不能为空")
    @ApiModelProperty("名称")
    private String keyName;

    @NotBlank(message = "keyValue 不能为空")
    @ApiModelProperty("值")
    private String keyValue;

    @ApiModelProperty("描述")
    private String description;
}
