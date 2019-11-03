package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.dto.request.PermissionListParam;
import com.lym.springboot.web.core.dto.request.PermissionSaveParam;
import com.lym.springboot.web.core.dto.request.PermissionUpdateParam;

/**
 * Created by liuyanmin on 2019/10/18.
 */
public interface IPermissionService {

    ApiResult save(PermissionSaveParam permissionSaveParam);

    ApiResult<PageBean<WebPermission>> list(PermissionListParam permissionListParam);

    ApiResult update(PermissionUpdateParam permissionUpdateParam);

    ApiResult<WebPermission> detail(Integer permissionId);
}
