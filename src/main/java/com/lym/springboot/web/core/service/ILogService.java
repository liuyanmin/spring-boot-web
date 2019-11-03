package com.lym.springboot.web.core.service;

import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebLog;

/**
 * Created by liuyanmin on 2019/9/30.
 */
public interface ILogService {

    /**
     * 列表
     */
    PageBean<WebLog> list(Integer pageNum, Integer pageSize);
}
