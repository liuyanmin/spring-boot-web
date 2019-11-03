package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dao.SystemDao;
import com.lym.springboot.web.core.domain.WebSystem;
import com.lym.springboot.web.core.dto.request.SystemListParam;
import com.lym.springboot.web.core.dto.request.SystemSaveParam;
import com.lym.springboot.web.core.dto.request.SystemUpdateParam;
import com.lym.springboot.web.core.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Service
public class SystemServiceImpl implements ISystemService {

    @Autowired
    private SystemDao systemDao;

    @Override
    public ApiResult<PageBean<WebSystem>> list(SystemListParam systemListParam) {
        PageBean<WebSystem> pageBean = systemDao.list(systemListParam);
        return ApiResult.ok(pageBean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult save(SystemSaveParam systemSaveParam) {
        WebSystem system = new WebSystem();
        system.setKeyName(systemSaveParam.getKeyName());
        system.setKeyValue(systemSaveParam.getKeyValue());
        system.setDescription(systemSaveParam.getDescription());
        systemDao.insert(system);
        return ApiResult.ok();
    }

    @Override
    public ApiResult<WebSystem> detail(Integer systemId) {
        WebSystem system = systemDao.getById(systemId);
        return ApiResult.ok(system);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult update(SystemUpdateParam systemUpdateParam) {
        WebSystem system = new WebSystem();
        system.setId(systemUpdateParam.getId());
        system.setKeyName(systemUpdateParam.getKeyName());
        system.setKeyValue(systemUpdateParam.getKeyValue());
        system.setDescription(systemUpdateParam.getDescription());
        systemDao.update(system);
        return ApiResult.ok();
    }
}
