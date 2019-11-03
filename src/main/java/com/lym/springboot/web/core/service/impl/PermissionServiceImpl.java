package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dao.PermissionDao;
import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.dto.request.PermissionListParam;
import com.lym.springboot.web.core.dto.request.PermissionSaveParam;
import com.lym.springboot.web.core.dto.request.PermissionUpdateParam;
import com.lym.springboot.web.core.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult save(PermissionSaveParam permissionSaveParam) {
        WebPermission permission = new WebPermission();
        permission.setPermissionName(permissionSaveParam.getPermissionName());
        permission.setDescription(permissionSaveParam.getDescription());
        permission.setStatus(permissionSaveParam.getStatus());

        permissionDao.insert(permission);
        return ApiResult.ok();
    }

    @Override
    public ApiResult<PageBean<WebPermission>> list(PermissionListParam permissionListParam) {
        PageBean<WebPermission> pageBean = permissionDao.list(permissionListParam);
        return ApiResult.ok(pageBean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult update(PermissionUpdateParam permissionUpdateParam) {
        WebPermission permission = new WebPermission();
        permission.setId(permissionUpdateParam.getPermissionId());
        permission.setPermissionName(permissionUpdateParam.getPermissionName());
        permission.setDescription(permissionUpdateParam.getDescription());
        permission.setStatus(permissionUpdateParam.getStatus());
        permission.setUpdateTime(LocalDateTime.now());
        permissionDao.update(permission);
        return ApiResult.ok();
    }

    @Override
    public ApiResult<WebPermission> detail(Integer permissionId) {
        WebPermission permission = permissionDao.getById(permissionId);
        return ApiResult.ok(permission);
    }
}
