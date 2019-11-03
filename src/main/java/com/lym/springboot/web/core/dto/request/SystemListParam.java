package com.lym.springboot.web.core.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Data
@ApiModel("系统参数查询")
public class SystemListParam {

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("筛选，名称")
    private String keyName;

    @ApiModelProperty("筛选，值")
    private String keyValue;

    @ApiModelProperty("筛选，描述")
    private String description;

}
