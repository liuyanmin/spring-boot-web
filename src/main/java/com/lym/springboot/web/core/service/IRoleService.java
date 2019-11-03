package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebRole;
import com.lym.springboot.web.core.dto.request.RoleListParam;
import com.lym.springboot.web.core.dto.request.RoleSaveParam;
import com.lym.springboot.web.core.dto.request.RoleUpdateParam;
import com.lym.springboot.web.core.dto.response.RoleDetailResponse;
import com.lym.springboot.web.core.vo.RoleMenuDetailResponseVo;

/**
 * Created by liuyanmin on 2019/10/18.
 */
public interface IRoleService {

    ApiResult save(RoleSaveParam roleSaveParam);

    ApiResult<PageBean<WebRole>> list(RoleListParam roleListParam);

    ApiResult<RoleDetailResponse> detail(Integer roleId);

    ApiResult update(RoleUpdateParam roleUpdateParam);

    ApiResult<RoleMenuDetailResponseVo> newPage();
}
