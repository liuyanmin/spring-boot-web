package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebSystem;
import com.lym.springboot.web.core.dto.request.SystemListParam;
import com.lym.springboot.web.core.dto.request.SystemSaveParam;
import com.lym.springboot.web.core.dto.request.SystemUpdateParam;

/**
 * Created by liuyanmin on 2019/10/22.
 */
public interface ISystemService {

    ApiResult<PageBean<WebSystem>> list(SystemListParam systemListParam);

    ApiResult save(SystemSaveParam systemSaveParam);

    ApiResult<WebSystem> detail(Integer systemId);

    ApiResult update(SystemUpdateParam systemUpdateParam);
}
