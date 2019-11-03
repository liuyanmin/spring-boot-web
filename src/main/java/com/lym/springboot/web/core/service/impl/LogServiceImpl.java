package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dao.LogDao;
import com.lym.springboot.web.core.domain.WebLog;
import com.lym.springboot.web.core.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuyanmin on 2019/9/30.
 */
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogDao logDao;

    @Override
    public PageBean list(Integer pageNum, Integer pageSize) {
        PageBean<WebLog> pageBean = logDao.list(pageNum, pageSize);
        return pageBean;
    }
}
