package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.core.dto.request.MenuSaveParam;
import com.lym.springboot.web.core.dto.request.MenuUpdateParam;
import com.lym.springboot.web.core.dto.response.MenuDetailResponse;
import com.lym.springboot.web.core.dto.response.MenuListResponse;
import com.lym.springboot.web.core.dto.response.MenuNewResponse;

import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
public interface IMenuService {

    ApiResult save(MenuSaveParam menuSaveParam);

    /**
     * 查询菜单列表（逐层查询）
     */
    ApiResult<List<MenuListResponse>> list(Integer menuId);

    ApiResult<MenuDetailResponse> detail(Integer menuId);

    ApiResult update(MenuUpdateParam menuUpdateParam);

    /**
     * 新建页面，查询菜单和权限数据
     */
    ApiResult<MenuNewResponse> newPage();

}
