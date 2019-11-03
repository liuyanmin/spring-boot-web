package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dto.request.*;
import com.lym.springboot.web.core.dto.response.AdminDetailResponse;
import com.lym.springboot.web.core.dto.response.AdminListResponse;
import com.lym.springboot.web.core.dto.response.AdminNewResponse;
import com.lym.springboot.web.core.dto.response.UserMenuListResponse;

/**
 * Created by liuyanmin on 2019/10/19.
 */
public interface IAdminService {

    /**
     * 用户菜单列表
     */
    ApiResult<UserMenuListResponse> userMenuList(Integer adminId);

    ApiResult<PageBean<AdminListResponse>> list(AdminListParam adminListParam);

    ApiResult save(AdminSaveParam adminSaveParam);

    /**
     * 新建，查询所有角色
     */
    ApiResult<AdminNewResponse> newPage();

    ApiResult<AdminDetailResponse> detail(Integer adminId);

    ApiResult update(AdminUpdateParam adminUpdateParam);

    /**
     * 找回密码
     */
    ApiResult findPassword(String username);

    /**
     * 确认找回密码
     */
    ApiResult confirmResetPassword(String u, String c);

    /**
     * 修改密码
     */
    ApiResult updatePassword(AdminUpdatePwdParam adminUpdatePwdParam);

    /**
     * 修改邮箱
     */
    ApiResult updateMail(AdminUpdateMailParam adminUpdateMailParam);
}
